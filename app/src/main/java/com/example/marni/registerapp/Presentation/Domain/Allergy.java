package com.example.marni.registerapp.Presentation.Domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Allergy implements Serializable, Parcelable {
    private String image_url;
    private String informationText;
    private Boolean checked = false;

    public Allergy(String image_url, String informationText){
        this.image_url = image_url;
        this.informationText = informationText;
    }

    protected Allergy(Parcel in) {
        image_url = in.readString();
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

    public String getImage_url(){
        return image_url;
    }

    public String getInformationText(){
        return informationText;
    }

    public void setImage(String imageid){
        this.image_url = imageid;
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

    }

    public Boolean checked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
