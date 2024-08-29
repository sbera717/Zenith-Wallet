package com.example.DAO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
public class ReturnUserDetail {
    private String name;
    private String email;
    private String mobile;
    private Date createdOn;
    private  Date updatedOn;
}
