package com.steparrik.entity;

import com.steparrik.entity.enums.RegisterType;
import com.steparrik.entity.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @OneToMany(mappedBy = "owner")
    private List<Task> ownTasks;

    @OneToMany(mappedBy = "executor")
    private List<Task> executeTasks;

    @OneToMany(mappedBy = "sender")
    private List<Comment> comments;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegisterType registerType;
}
