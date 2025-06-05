package site.wijerathne.harshana.edupanel.to;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import site.wijerathne.harshana.edupanel.uitl.LecturerType;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecturerTO implements Serializable {
    @Null(message = "Id should be empty")
    private Integer id;
    @NotBlank(message = "Name can't be empty")
    @Pattern(regexp = "^[A-Za-z ]{2,}$", message = "Invalid name")
    private String name;
    @NotBlank(message = "Designation can't be empty")
    @Length(min = 3, message = "Invalid designation")
    private String designation;
    @NotBlank(message = "Qualifications can't be empty")
    @Length(min = 3, message = "Invalid qualifications")
    private String qualifications;
    @NotNull(message = "Type should be either full-time or visiting")
    private LecturerType type;
    @NotNull(message = "Display order can't be empty")
    @PositiveOrZero(message = "Invalid display order")
    private Integer displayOrder;
    @Null(message = "Picture should be empty")
    private String picture;
    @Pattern(regexp = "^http(s)://.+$", message = "Invalid linkedin url")
    private String linkedin;

}
