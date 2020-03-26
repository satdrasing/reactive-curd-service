package com.satendra.employeeservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Employee implements Serializable {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String dept;

    @CreatedDate
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();


}
