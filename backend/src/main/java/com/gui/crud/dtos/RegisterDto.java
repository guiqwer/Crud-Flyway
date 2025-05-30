package com.gui.crud.dtos;


import com.gui.crud.model.user.UserRole;

public record RegisterDto(String email, String password, UserRole userRole) {
}
