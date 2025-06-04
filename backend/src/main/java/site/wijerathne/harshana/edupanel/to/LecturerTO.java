package site.wijerathne.harshana.edupanel.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.wijerathne.harshana.edupanel.uitl.LecturerType;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecturerTO implements Serializable {
    private  Integer id;
    private  String name;
    private  String designation;
    private  String qualifications;
    private  LecturerType type;
    private  Integer displayOrder;
    private  String picture;
    private  String linkedin;

}
