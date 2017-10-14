package com.example.android.travelandtourism.Models;

/**
 * Created by haya on 04/09/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Roles extends RealmObject {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("roleId")
    @Expose
    private String roleId;
    @SerializedName("role")
    @Expose
    private Role_ role;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Role_ getRole() {
        return role;
    }

    public void setRole(Role_ role) {
        this.role = role;
    }

}