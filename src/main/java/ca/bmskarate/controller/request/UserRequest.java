package ca.bmskarate.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest {
    long id;
    String emailId;
    String password;
    long phone;
    String firstName;
    String lastName;
    String addr1;
    String addr2;
    String postalCode;
    String premium;
    String secretQues;
    String secretAns;
    int cityId;

    String sesnei;
    String type;

    String oldPassword;
    String confirmPassword;
}
