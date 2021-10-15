package vn.name.trungnhan.eword;
/**
 * @desc Ứng dụng học từ vựng tiếng Anh EWORD
 * @author Nguyễn Trung Nhẫn trungnhan0911@yandex.com
 * @coppyright 2021 - MIT Lisence
 */
public class TV_ADD {
    private String countryName;

    // Image name (Without extension)
    private String flagName;
    private String mota;

    public TV_ADD(String countryName, String flagName, String mota) {
        this.countryName= countryName;
        this.flagName= flagName;
        this.mota= mota;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getFlagName() {
        return flagName;
    }

    public void setFlagName(String flagName) {
        this.flagName = flagName;
    }

    @Override
    public String toString()  {
        return this.countryName;//+" (Intro: "+ this.mota+")";
    }
}
