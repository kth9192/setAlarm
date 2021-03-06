package com.noname.setalarm.view;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ImageProvider extends BaseObservable {
    private int amPic;
    private int pmPic;

    public ImageProvider(int amPic, int pmPic) {
        this.amPic = amPic;
        this.pmPic = pmPic;
    }

    @Bindable
    public int getAmPic() {
        return amPic;
    }

    @Bindable
    public int getPmPic() {
        return pmPic;
    }
}
