package sqb.uz.task.helper;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class Convert {
    public static Double toDou(String value){
        try {
            return Double.parseDouble(value);
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    public static Integer toInt(String value){
        try {
            return Integer.parseInt(value);
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    public static Boolean toBoo(String value){
        try {
            return Boolean.parseBoolean(value);
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    public static LocalDate toDat(String value){
        try {
            return LocalDate.parse(value);
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }
}
