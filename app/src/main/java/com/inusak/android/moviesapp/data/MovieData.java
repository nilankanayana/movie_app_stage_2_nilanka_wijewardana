package com.inusak.android.moviesapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class holds Movie Data.
 * <p>
 * Created by Nilanka on 10/12/2017.
 */

public class MovieData implements Parcelable {

    public static final String MOVIE_DATA_KEY = "MOVIE_DATA";

    public static final String DEFAULT_IMAGE_SIZE = "w185";

    private long id;

    private String title;

    private String releaseDate;

    private String posterPath;

    private String backdropPath;

    private double averageVotes;

    private String overview;

    private String voteCount;

    private String genres;

    private String languages;

    private boolean liked;

    private byte[] posterImageData;

    private byte[] backdropImageData;

    /**
     * Parameterized constructor
     *
     * @param id
     * @param title
     * @param releaseDate
     * @param posterPath
     * @param backdropPath
     * @param averageVotes
     * @param voteCount
     * @param overview
     * @param genres
     * @param languages
     */
    public MovieData(long id, String title, String releaseDate, String posterPath, String backdropPath, double averageVotes, String voteCount, String overview, String genres, String languages) {
        this(id, title, releaseDate, posterPath, backdropPath, averageVotes, voteCount, overview, genres, languages, null, null, false);
    }

    /**
     * Parameterized constructor
     *
     * @param id
     * @param title
     * @param releaseDate
     * @param posterPath
     * @param backdropPath
     * @param averageVotes
     * @param voteCount
     * @param overview
     * @param genres
     * @param languages
     * @param posterImageData
     * @param backdropImageData
     * @param liked
     */
    public MovieData(long id, String title, String releaseDate, String posterPath, String backdropPath, double averageVotes, String voteCount, String overview,
                     String genres, String languages, byte[] posterImageData, byte[] backdropImageData, boolean liked) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.averageVotes = averageVotes;
        this.voteCount = voteCount;
        this.overview = overview;
        this.genres = genres;
        this.languages = languages;
        this.posterImageData = posterImageData;
        this.backdropImageData = backdropImageData;
        this.liked = liked;
    }

    /**
     * Constructor for creating data from parcel
     *
     * @param parcel
     */
    private MovieData(Parcel parcel) {
        readFromParcel(parcel);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public double getAverageVotes() {
        return averageVotes;
    }

    public String getOverview() {
        return overview;
    }

    public byte[] getPosterImageData() {
        return posterImageData;
    }

    public byte[] getBackdropImageData() {
        return backdropImageData;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public String getLanguages() {
        return languages;
    }

    public String getGenres() {
        return genres;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(getTitle());
        dest.writeString(getReleaseDate());
        dest.writeString(getPosterPath());
        dest.writeString(getBackdropPath());
        dest.writeDouble(getAverageVotes());
        dest.writeString(getVoteCount());
        dest.writeString(getOverview());
        dest.writeString(getGenres());
        dest.writeString(getLanguages());
//        if (getPosterImageData() != null) {
//            dest.writeInt(getPosterImageData().length);
//            dest.writeByteArray(getPosterImageData());
//        }
//        if (getBackdropImageData() != null) {
//            dest.writeInt(getBackdropImageData().length);
//            dest.writeByteArray(getBackdropImageData());
//        }
        dest.writeString(String.valueOf(isLiked()));
    }

    public void readFromParcel(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.averageVotes = in.readDouble();
        this.voteCount = in.readString();
        this.overview = in.readString();
        this.genres = in.readString();
        this.languages = in.readString();
//        int length = in.readInt();
//        if (length > -1) {
//            this.posterImageData = new byte[length];
//            in.readByteArray(this.posterImageData);
//        }
//        length = in.readInt();
//        if (length > -1) {
//            this.backdropImageData = new byte[length];
//            in.readByteArray(this.backdropImageData);
//        }
        this.liked = Boolean.parseBoolean(in.readString());
    }

    public static final Creator<MovieData> CREATOR = new Creator() {

        @Override
        public MovieData createFromParcel(Parcel source) {
            return new MovieData(source);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };


}
