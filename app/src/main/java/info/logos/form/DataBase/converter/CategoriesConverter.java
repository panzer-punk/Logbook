package info.logos.form.DataBase.converter;

import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;


import androidx.room.TypeConverter;

public class CategoriesConverter {

    @TypeConverter
    public String fromCategories(List<Integer> categories) {
        return TextUtils.join(", ", categories);
    }

    @TypeConverter
    public List<Integer> toCategories(String data) {
        return Arrays.asList(Integer.parseInt(String.valueOf(data.split(","))));
    }

}
