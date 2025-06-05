package site.wijerathne.harshana.edupanel.api;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import site.wijerathne.harshana.edupanel.entity.Lecturer;
import site.wijerathne.harshana.edupanel.entity.LinkedIn;
import site.wijerathne.harshana.edupanel.entity.Picture;
import site.wijerathne.harshana.edupanel.to.LecturerTO;
import site.wijerathne.harshana.edupanel.to.request.LecturerReqTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/lecturers")
@CrossOrigin
public class LecturerHttpController {

    Dotenv dotenv = Dotenv.load();
    Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));

    // Set your Cloudinary credentials
    Map params1 = ObjectUtils.asMap(
            "use_filename", true,
            "unique_filename", false,
            "overwrite", true
    );
    @Autowired
    private EntityManager em;
    @Autowired
    private ModelMapper mapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public LecturerTO createNewLecturer(@ModelAttribute @Validated(LecturerReqTO.Create.class) LecturerReqTO lecturerReqTO) throws IOException {
        em.getTransaction().begin();
        try {
            Lecturer lecturer = mapper.map(lecturerReqTO, Lecturer.class);
            lecturer.setPicture(null);
            lecturer.setLinkedIn(null);
            em.persist(lecturer);
            LecturerTO lecturerTO = mapper.map(lecturer, LecturerTO.class);

            if (lecturerReqTO.getLinkedin() != null) {
                em.persist(new LinkedIn(lecturer, lecturerReqTO.getLinkedin()));
                lecturerTO.setLinkedin(lecturerReqTO.getLinkedin());
            }


            if (lecturerReqTO.getPicture() != null && !lecturerReqTO.getPicture().isEmpty()) {
                // Upload the image to Cloudinary
                Map uploadResult = cloudinary.uploader().upload(
                        lecturerReqTO.getPicture().getBytes(),
                        ObjectUtils.asMap(
                                "public_id", "lecturers/" + lecturer.getId(),
                                "use_filename", true,
                                "unique_filename", false,
                                "overwrite", true
                        )
                );
                lecturerTO.setPicture(uploadResult.get("url").toString());
//                System.out.println(uploadResult);

                // Save the picture reference in your database
                Picture picture = new Picture(lecturer, (String) uploadResult.get("public_id"));
                em.persist(picture);
            }
            System.out.println(lecturerTO);
            em.getTransaction().commit();
            return lecturerTO;
        } catch (Throwable e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}", consumes = "multipart/form-data")
    public void updateLecturerDetailsViaMultipart(@PathVariable("lecturer-id") Integer lecturerId,
                                                  @ModelAttribute @Validated(LecturerReqTO.Update.class) LecturerReqTO lecturerReqTO) throws IOException {
        Lecturer currentLecturer = em.find(Lecturer.class, lecturerId);
        if (currentLecturer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        em.getTransaction().begin();
        try {
            Lecturer newLecturer = mapper.map(lecturerReqTO, Lecturer.class);
            newLecturer.setId(lecturerId);
            newLecturer.setPicture(null);
            newLecturer.setLinkedIn(null);

            if(lecturerReqTO.getPicture() != null && currentLecturer.getPicture() == null){
                newLecturer.setPicture(new Picture(newLecturer,"lecturers/"+lecturerId));
                em.persist(newLecturer.getPicture());
                cloudinary.uploader().upload(lecturerReqTO.getPicture().getBytes(),
                        ObjectUtils.asMap(
                                "public_id", "lecturers/" + newLecturer.getId(),
                                "use_filename", true,
                                "unique_filename", false,
                                "overwrite", true
                        )
                );

            } else if (lecturerReqTO.getPicture() == null && currentLecturer.getPicture() != null) {
                em.remove(currentLecturer.getPicture());
                cloudinary.uploader().destroy("lecturers/"+lecturerId, ObjectUtils.emptyMap());

            }else {
                newLecturer.setPicture(new Picture(newLecturer,"lecturers/"+lecturerId));
                em.merge(newLecturer.getPicture());
                cloudinary.uploader().upload(lecturerReqTO.getPicture().getBytes(),
                        ObjectUtils.asMap(
                                "public_id", "lecturers/" + newLecturer.getId(),
                                "use_filename", true,
                                "unique_filename", false,
                                "overwrite", true
                        )
                );
            }

            if(lecturerReqTO.getLinkedin() != null && currentLecturer.getLinkedIn() == null){
                    newLecturer.setLinkedIn(new LinkedIn(newLecturer,lecturerReqTO.getLinkedin()));
                    em.persist(newLecturer.getLinkedIn());
            } else if (lecturerReqTO.getLinkedin() == null && currentLecturer.getLinkedIn() != null) {
                    em.remove(currentLecturer.getLinkedIn());
            }else {
                newLecturer.setLinkedIn(new LinkedIn(newLecturer,lecturerReqTO.getLinkedin()));
                em.merge(newLecturer.getLinkedIn());
            }

            em.merge(newLecturer);
            em.getTransaction().commit();
        } catch (Throwable e) {
            em.getTransaction().rollback();
            throw e;
        }
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}", consumes = "application/json")
    public void updateLecturerDetailsViaJson(@PathVariable("lecturer-id") Integer lecturerId,@RequestBody @Validated LecturerTO lecturerTO) throws IOException {
        Lecturer currentLecturer = em.find(Lecturer.class, lecturerId);
        if(currentLecturer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        em.getTransaction().begin();
        try {
            Lecturer newLecturer = mapper.map(lecturerTO, Lecturer.class);
            newLecturer.setId(lecturerId);
            newLecturer.setPicture(currentLecturer.getPicture());
            newLecturer.setLinkedIn(lecturerTO.getLinkedin() != null ? new LinkedIn(newLecturer,lecturerTO.getLinkedin()) : null);

            if(currentLecturer.getLinkedIn() != null && newLecturer.getLinkedIn() == null) {
                em.remove(currentLecturer.getLinkedIn());
            }else if (currentLecturer.getLinkedIn() == null && newLecturer.getLinkedIn() != null) {
                em.persist(newLecturer.getLinkedIn());
            }else if (newLecturer.getLinkedIn() != null) {
                em.merge(newLecturer.getLinkedIn());
            }


            em.merge(newLecturer);
            em.getTransaction().commit();
        }catch (Throwable e){
            em.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{lecturer-id}")
    public void deleteLecturer(@PathVariable("lecturer-id") Integer lecturerId) {
        Lecturer lecturer = em.find(Lecturer.class, lecturerId);
        if (lecturer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        em.getTransaction().begin();
        try {
            if (lecturer.getPicture() != null) {
                String publicId = "lecturers/" + lecturer.getPicture().getLecturer().getId(); // e.g., "lecturers/27"
                try {
                    Map<?, ?> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                    System.out.println("Cloudinary delete result: " + result);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete image from Cloudinary", e);
                }
            }
            em.remove(lecturer);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    @GetMapping(produces = "application/json")
    public List<LecturerTO> getAllLecturers() {
        TypedQuery<Lecturer> query = em.createQuery("SELECT l FROM Lecturer l", Lecturer.class);
        return getLecturerTOList(query);
    }


    @GetMapping(path = "/{lecturer-id}", produces = "application/json")
    public LecturerTO getLecturerDetails(@PathVariable("lecturer-id") Integer lecturerId) {
        Lecturer lecturer = em.find(Lecturer.class, lecturerId);
        if (lecturer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        LecturerTO lecturerTO = mapper.map(lecturer, LecturerTO.class);
        if (lecturer.getLinkedIn() != null) {
            lecturerTO.setLinkedin(lecturer.getLinkedIn().getUrl());
        }
        if (lecturer.getPicture() != null) {
            int id = lecturer.getPicture().getLecturer().getId();
            String url = cloudinary.url().secure(false)
                    .generate("lecturers/" + id);
            lecturerTO.setPicture(url);
        }
        return lecturerTO;
    }

    @GetMapping(params = "type=full-time", produces = "application/json")
    public List<LecturerTO> getFullTimeLecturers() {
        TypedQuery<Lecturer> query = em.createQuery("SELECT l FROM Lecturer l where l.type = site.wijerathne.harshana.edupanel.uitl.LecturerType.FULL_TIME", Lecturer.class);
        return getLecturerTOList(query);
    }

    @GetMapping(params = "type=visiting", produces = "application/json")
    public List<LecturerTO> getVisitingLecturers() {
        TypedQuery<Lecturer> query = em.createQuery("SELECT l FROM Lecturer l where l.type = site.wijerathne.harshana.edupanel.uitl.LecturerType.VISITING", Lecturer.class);
        return getLecturerTOList(query);
    }

    @GetMapping("/hi")
    public void hi() {
        System.out.println("hi");
    }

    private List<LecturerTO> getLecturerTOList(TypedQuery<Lecturer> query) {
        return query.getResultStream().map(lecturerEntity -> {
            LecturerTO lecturerTO = mapper.map(lecturerEntity, LecturerTO.class);
            if (lecturerEntity.getLinkedIn() != null) {
                lecturerTO.setLinkedin(lecturerEntity.getLinkedIn().getUrl());
            }
            if (lecturerEntity.getPicture() != null) {
                int id = lecturerEntity.getPicture().getLecturer().getId();
                String url = cloudinary.url().secure(false)
                        .generate("lecturers/" + id);
                lecturerTO.setPicture(url);
            }
            return lecturerTO;
        }).toList();
    }
}
