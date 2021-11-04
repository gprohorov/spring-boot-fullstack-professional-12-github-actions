package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


/*
  @author   george
  @project   spring-boot-fullstack-professional-12-github-actions
  @class  StudentServiceTest
  @version  1.0.0 
  @since 03.11.21 - 12.14
*/

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository repository;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(repository);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void getAllStudents() {
        // when
        underTest.getAllStudents();
        // then
        verify(repository).findAll();
    }

    @Test
    void whenEmailIsNotTakenAddStudent() {
        //given
        String email = "ivan@gmail.com";
        Student student = new Student(
                "Ivan",
                email,
                Gender.MALE
        );
        // when
        underTest.addStudent(student);
        //then
        ArgumentCaptor<Student> argumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(repository).save(argumentCaptor.capture());
        Student captured = argumentCaptor.getValue();
        assertThat(captured).isEqualTo(student);

    }

    @Test
    void whenEmailIsTakenThrowException() {
        //given
        String email = "ivan@gmail.com";
        Student student = new Student(
                "Ivan",
                email,
                Gender.MALE
        );
        given(repository.selectExistsEmail(student.getEmail())).willReturn(true);
        // when
        // then
        assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("taken");
        verify(repository, never()).save(any());

    }

    @Test
    void whenIsPresentDeleteStudent() {
        //given
        Long id = 1l;
        given(repository.existsById(id)).willReturn(true);
        // when
      underTest.deleteStudent(id);
       // then
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(repository).deleteById(argumentCaptor.capture());
        Long captured = argumentCaptor.getValue();
        assertThat(captured).isEqualTo(id);
    }
    @Test
    void whenIsNotPresentThrowException() {
        //given
        Long id = 1l;
        given(repository.existsById(id)).willReturn(false);
        // when
    //  underTest.deleteStudent(id);
       // then
        assertThatThrownBy(() -> underTest.deleteStudent(id))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("not");
    }
}