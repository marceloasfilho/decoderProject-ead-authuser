package com.ead.authuser.dto;

import com.ead.authuser.constraint.UsernameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private UUID userId;
    @JsonView(UserView.RegisterUser.class)
    @NotBlank(groups = UserView.RegisterUser.class)
    @Size(min = 4, max = 50, groups = UserView.RegisterUser.class)
    @UsernameConstraint(groups = UserView.RegisterUser.class)
    private String username;

    @JsonView(UserView.RegisterUser.class)
    @NotBlank(groups = UserView.RegisterUser.class)
    @Email(groups = UserView.RegisterUser.class)
    private String email;

    @JsonView({UserView.RegisterUser.class, UserView.UpdatePassword.class})
    @NotBlank(groups = {UserView.RegisterUser.class, UserView.UpdatePassword.class})
    @Size(min = 6, max = 20, groups = {UserView.RegisterUser.class, UserView.UpdatePassword.class})
    private String password;

    @JsonView(UserView.UpdatePassword.class)
    @NotBlank(groups = UserView.UpdatePassword.class)
    @Size(min = 6, max = 20, groups = UserView.UpdatePassword.class)
    private String oldPassword;

    @JsonView({UserView.RegisterUser.class, UserView.UpdateUser.class})
    private String fullName;

    @JsonView({UserView.RegisterUser.class, UserView.UpdateUser.class})
    private String phoneNumber;

    @JsonView({UserView.RegisterUser.class, UserView.UpdateUser.class})
    private String cpf;

    @JsonView(UserView.UpdateImageUrl.class)
    @NotBlank(groups = UserView.UpdateImageUrl.class)
    private String imageUrl;

    public interface UserView {
        interface RegisterUser {
        }

        interface UpdateUser {
        }

        interface UpdatePassword {
        }

        interface UpdateImageUrl {
        }
    }
}
