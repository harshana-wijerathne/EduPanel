package site.wijerathne.harshana.edupanel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import site.wijerathne.harshana.edupanel.uitl.LecturerType;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lecturer")
public class Lecturer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, length = 300)
    private String name;
    @Column(nullable = false, length = 600)
    private String designation;
    @Column(nullable = false, length = 600)
    private String qualifications;
    @Column(nullable = false, columnDefinition = "ENUM('FULL_TIME','VISITING')")
    @Enumerated(EnumType.STRING)
    private LecturerType type;
    @Column(nullable = false, name = "display_order")
    private int displayOrder;

    @ToString.Exclude
    @OneToOne(mappedBy = "lecturer",cascade = {CascadeType.REMOVE,CascadeType.PERSIST})
    private Picture picture;

    @ToString.Exclude
    @OneToOne(mappedBy = "lecturer",cascade = {CascadeType.REMOVE,CascadeType.PERSIST})
    private LinkedIn linkedIn;

    public Lecturer(String name, String designation, String qualifications, LecturerType type, int displayOrder) {
        this.name = name;
        this.designation = designation;
        this.qualifications = qualifications;
        this.type = type;
        this.displayOrder = displayOrder;
    }

    public Lecturer(int id, String name, String designation, String qualifications, LecturerType type, int displayOrder) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.qualifications = qualifications;
        this.type = type;
        this.displayOrder = displayOrder;
    }

    public void setLinkedIn(LinkedIn linkedIn) {
        if(linkedIn != null) linkedIn.setLecturer(this);
        this.linkedIn = linkedIn;
    }

    public void setPicture(Picture picture) {
        if(picture != null) picture.setLecturer(this);
        this.picture = picture;
    }
}
