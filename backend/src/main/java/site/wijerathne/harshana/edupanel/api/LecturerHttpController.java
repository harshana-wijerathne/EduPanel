package site.wijerathne.harshana.edupanel.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/a1/lecturers")
@CrossOrigin
public class LecturerHttpController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "multipart/form-data",produces = "application/json")
    public void createNewLecturer() {}

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}",consumes = "application/json")
    public void updateLecturerDetailsViaMultipart(@PathVariable("lecturer-id") Integer lecturerId) {}


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}",consumes = "application/json")
    public void updateLecturerDetailsViaJson(@PathVariable("lecturer-id") Integer lecturerId) {}

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{lecturer-id}")
    public void deleteLecturer(@PathVariable("lecturer-id") Integer lecturerId){}

    @GetMapping(consumes = "application/json")
    public void getAllLecturers(){}


    @GetMapping(path = "/{lecturer-id}",consumes = "application/json")
    public void getLecturerDetails(@PathVariable("lecturer-id") Integer lecturerId){}

    @GetMapping(params = "type=full-time",consumes = "application/json")
    public void getFullTimeLecturers(){}

    @GetMapping(params = "type=visiting",consumes = "application/json")
    public void getVisitingLecturers(){}
}
