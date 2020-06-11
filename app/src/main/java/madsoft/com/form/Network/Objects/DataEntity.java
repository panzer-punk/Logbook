package madsoft.com.form.Network.Objects;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.Nullable;

public interface DataEntity extends Serializable {

    Integer getId();
    @Nullable
    String getModified();
    @Nullable
    String getMediaUrl();
    List<Integer> getCategories();
    String getTitleS();
    String getUrl();
}
