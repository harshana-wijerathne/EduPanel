package site.wijerathne.harshana.edupanel.converter;

import org.springframework.core.convert.converter.Converter;
import site.wijerathne.harshana.edupanel.uitl.LecturerType;

public class LectureTypeConverter implements Converter<String, LecturerType> {


    @Override
    public LecturerType convert(String source) {
        for (LecturerType type : LecturerType.values()) {
            if (type.getType().equals(source)) {
                return type;
            }
        }
        return null;
    }
}
