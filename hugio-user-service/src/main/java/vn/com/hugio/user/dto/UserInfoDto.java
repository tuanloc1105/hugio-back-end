package vn.com.hugio.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.dto.BaseEntityDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserInfoDto extends BaseEntityDto {

    @JsonProperty("user_uid")
    private String userUid;

    @JsonProperty("cif")
    private String cif;

    @JsonProperty("email")
    private String email;

    @JsonProperty("address")
    private String address;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("username")
    private String username;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("roles")
    private List<String> roles;

    public void merge(UserInfoDto anotherDto) {
        super.mergeBase(anotherDto);
        this.userUid = Optional.ofNullable(anotherDto.getUserUid()).isPresent() ? anotherDto.getUserUid() : this.userUid;
        this.cif = Optional.ofNullable(anotherDto.getCif()).isPresent() ? anotherDto.getCif() : this.cif;
        this.email = Optional.ofNullable(anotherDto.getEmail()).isPresent() ? anotherDto.getEmail() : this.email;
        this.address = Optional.ofNullable(anotherDto.getAddress()).isPresent() ? anotherDto.getAddress() : this.address;
        this.fullName = Optional.ofNullable(anotherDto.getFullName()).isPresent() ? anotherDto.getFullName() : this.fullName;
        this.username = Optional.ofNullable(anotherDto.getUsername()).isPresent() ? anotherDto.getUsername() : this.username;
        this.phoneNumber = Optional.ofNullable(anotherDto.getPhoneNumber()).isPresent() ? anotherDto.getPhoneNumber() : this.phoneNumber;
        this.roles = Optional.ofNullable(anotherDto.getRoles()).isPresent() ? anotherDto.getRoles() : this.roles;
    }

    @Builder
    public UserInfoDto(Long id, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, String userUid, String cif, String email, String address, String fullName, String username, List<String> roles) {
        super(id, active, createdAt, updatedAt, createdBy, updatedBy);
        this.userUid = userUid;
        this.cif = cif;
        this.email = email;
        this.address = address;
        this.fullName = fullName;
        this.username = username;
        this.roles = roles;
    }
}
