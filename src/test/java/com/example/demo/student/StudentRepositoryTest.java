package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/*
  @author   george
  @project   spring-boot-fullstack-professional-12-github-actions
  @class  StudentRepositoryTest
  @version  1.0.0 
  @since 03.11.21 - 10.49
*/

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckIfStudentExistsByEmail() {
        // given
        String email = "ivan@gmail.com";
        Student student = new Student(
                "Ivan",
                email,
                Gender.MALE
        );
        underTest.save(student);
        System.out.println("-----------------TEST1--------------------------");
        System.out.println(underTest.findAll());
        //when
        Boolean expected = underTest.selectExistsEmail(email);
        //then
        assertThat(expected).isTrue();

    }

    @Test
    void itShouldCheckIfStudentDoesNotExistByEmail() {
        // given
        String email = "ivan@gmail.com";
        System.out.println("-----------------TEST2--------------------------");
        System.out.println(underTest.findAll());
        //when
        Boolean expected = underTest.selectExistsEmail(email);
        //then
        assertThat(expected).isFalse();

    }




}