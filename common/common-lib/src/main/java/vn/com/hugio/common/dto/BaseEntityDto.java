package vn.com.hugio.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class BaseEntityDto {


    @JsonProperty("id")
    private Long id;

    @JsonProperty("active")
    private boolean active;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("updated_by")
    private String updatedBy;

    public void mergeBase(BaseEntityDto anotherDto) {
        this.id = Optional.ofNullable(anotherDto.getId()).isPresent() ? anotherDto.getId() : this.id;
        this.active = anotherDto.isActive();
        this.createdAt = Optional.ofNullable(anotherDto.getCreatedAt()).isPresent() ? anotherDto.getCreatedAt() : this.createdAt;
        this.updatedAt = Optional.ofNullable(anotherDto.getUpdatedAt()).isPresent() ? anotherDto.getUpdatedAt() : this.updatedAt;
        this.createdBy = Optional.ofNullable(anotherDto.getCreatedBy()).isPresent() ? anotherDto.getCreatedBy() : this.createdBy;
        this.updatedBy = Optional.ofNullable(anotherDto.getUpdatedBy()).isPresent() ? anotherDto.getUpdatedBy() : this.updatedBy;
    }

}
