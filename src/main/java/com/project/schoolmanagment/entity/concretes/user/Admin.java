package com.project.schoolmanagment.entity.concretes.user;

import com.project.schoolmanagment.entity.abstracts.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "admins")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder // we don't need to call get set ets. we use superbuilder for inheritance, if there is parent child, both are superbuilder
public class Admin extends User {

    private boolean builtIn; //only extra property that exists in admin
}
