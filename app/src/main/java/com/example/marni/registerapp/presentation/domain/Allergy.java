package com.example.marni.registerapp.presentation.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Allergy implements Serializable, Parcelable {
    private String imageurl;
    private String informationText;
    private Boolean checked = false;

    public Allergy(String imageurl, String informationText){
        this.imageurl = imageurl;
        this.informationText = informationText;
    }

    protected Allergy(Parcel in) {
        imageurl = in.readString();
        informationText = in.readString();
    }

    public static final Creator<Allergy> CREATOR = new Creator<Allergy>() {
        @Override
        public Allergy createFromParcel(Parcel in) {
            return new Allergy(in);
        }

        @Override
        public Allergy[] newArray(int size) {
            return new Allergy[size];
        }
    };

    public String getImageurl(){
        return imageurl;
    }

    public String getInformationText(){
        return informationText;
    }

    public void setImage(String imageid){
        this.imageurl = imageid;
    }

    public void setInformationText(String informationText){
        this.informationText = informationText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //do nothing
    }

    public Boolean checked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
