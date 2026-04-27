package org.example.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "todos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nội dung công việc không được để trống")
    private String content;

    @FutureOrPresent(message = "Ngày hết hạn phải là hôm nay hoặc trong tương lai")
    private LocalDate dueDate;

    private String status = "PENDING";

    private String priority = "MEDIUM";
}