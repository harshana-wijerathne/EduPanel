package site.wijerathne.harshana.edupanel.to.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.groups.Default;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;
import site.wijerathne.harshana.edupanel.uitl.LecturerType;
import site.wijerathne.harshana.edupanel.validation.LecturerImage;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecturerReqTO implements Serializable {
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

    @Null(groups = Create.class, message = "Display order should be empty")
    @NotNull(groups = Update.class, message = "Display order can't be empty")
    @PositiveOrZero(groups = Update.class, message = "Invalid display order")
    private Integer displayOrder;

    @LecturerImage
    private MultipartFile picture;

    @Pattern(regexp = "^http(s)://.+$", message = "Invalid linkedin url")
    private String linkedin;

    public interface Create extends Default {}
    public interface Update extends Default{}
}
