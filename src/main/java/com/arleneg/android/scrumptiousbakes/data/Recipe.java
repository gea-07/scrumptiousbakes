package com.arleneg.android.scrumptiousbakes.data;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// This class was created and coded by http://www.jsonschema2pojo.org/
public class Recipe implements Parcelable
{
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("ingredients")
    @Expose
    private ArrayList<Ingredient> ingredients = null;

    @SerializedName("steps")
    @Expose
    private ArrayList<Step> steps = null;

    @SerializedName("servings")
    @Expose
    private long servings;

    @SerializedName("image")
    @Expose
    private String image;

    private int resourceId;

    public final static Parcelable.Creator<Recipe> CREATOR = new Creator<Recipe>() {

        @SuppressWarnings({
                "unchecked"
        })
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return (new Recipe[size]);
        }

    };

    protected Recipe(Parcel in) {
        this.id = ((long) in.readValue((long.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.ingredients = new ArrayList<>();
        in.readList(this.ingredients, (Ingredient.class.getClassLoader()));
        this.steps = new ArrayList<>();
        in.readList(this.steps, (Step.class.getClassLoader()));
        this.servings = ((long) in.readValue((long.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.resourceId = ((int) in.readValue((int.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Recipe() {
    }

    /**
     *
     * @param ingredients
     * @param id
     * @param servings
     * @param name
     * @param image
     * @param steps
     */
    public Recipe(long id, String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps, long servings, String image) {
        super();
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public long getServings() {
        return servings;
    }

    public void setServings(long servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeList(ingredients);
        dest.writeList(steps);
        dest.writeValue(servings);
        dest.writeValue(image);
        dest.writeValue(resourceId);
    }

    public int describeContents() {
        return 0;
    }

}