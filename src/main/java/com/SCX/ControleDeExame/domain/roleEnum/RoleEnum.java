package com.SCX.ControleDeExame.domain.roleEnum;

public enum RoleEnum {
    ADMIN("admin"),
    DOCTOR("doctor"),
    LABORATORY("laboratory"),
    PATIENT("patient"),
    SECRETARY("secretary");

    private String role;

    RoleEnum(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }



}
