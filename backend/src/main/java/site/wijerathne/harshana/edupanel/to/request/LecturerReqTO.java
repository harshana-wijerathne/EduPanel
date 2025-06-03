package site.wijerathne.harshana.edupanel.to.request;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import site.wijerathne.harshana.edupanel.uitl.LecturerType;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecturerReqTO implements Serializable {
    private String name;
    private String designation;
    private String qualifications;
    private LecturerType type;
    private Integer displayOrder;
    private MultipartFile picture;
    private String linkedin;
}
