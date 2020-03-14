package com.bsmart.pos.rider.tools;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;

import java.util.Arrays;
import java.util.List;

/**
 * Author: yoda
 * DateTime: 2020/3/3 11:59
 */
public final class StringUtil {

    private static final String FIELD_SEPARATOR = "\n";
    private static final String RESULT_SEPARATOR = "\n---\n\t";

    public static boolean isEmpty(String str){

        if (null == str || str.length()==0){
            return true;
        }else{
            return false;
        }

    }

    public static boolean isNotEmpty(String str){

        return !isEmpty(str);

    }

    static void prepend(TextView textView, String prefix) {
        textView.setText(prefix + "\n\n" + textView.getText());
    }

    @Nullable
    public static LatLngBounds convertToLatLngBounds(
            @Nullable String southWest, @Nullable String northEast) {
        LatLng soundWestLatLng = convertToLatLng(southWest);
        LatLng northEastLatLng = convertToLatLng(northEast);
        if (soundWestLatLng == null || northEast == null) {
            return null;
        }

        return new LatLngBounds(soundWestLatLng, northEastLatLng);
    }

    @Nullable
    public static LatLng convertToLatLng(@Nullable String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        String[] split = value.split(",", -1);
        if (split.length != 2) {
            return null;
        }

        try {
            return new LatLng(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
        } catch (NullPointerException | NumberFormatException e) {
            return null;
        }
    }

    public static List<String> countriesStringToArrayList(String countriesString) {
        // Allow these delimiters: , ; | / \
        List<String> countries = Arrays.asList(countriesString
                .replaceAll("\\s", "|")
                .split("[,;|/\\\\]",-1));

        return countries;
    }

    public static String stringify(FindAutocompletePredictionsResponse response, boolean raw) {
        StringBuilder builder = new StringBuilder();

        builder
                .append(response.getAutocompletePredictions().size())
                .append(" Autocomplete Predictions Results:");

        if (raw) {
            builder.append(RESULT_SEPARATOR);
            appendListToStringBuilder(builder, response.getAutocompletePredictions());
        } else {
            for (AutocompletePrediction autocompletePrediction : response.getAutocompletePredictions()) {
                builder
                        .append(RESULT_SEPARATOR)
                        .append(autocompletePrediction.getFullText(/* matchStyle */ null));
            }
        }

        return builder.toString();
    }

    static String stringify(FetchPlaceResponse response, boolean raw) {
        StringBuilder builder = new StringBuilder();

        builder.append("Fetch Place Result:").append(RESULT_SEPARATOR);
        if (raw) {
            builder.append(response.getPlace());
        } else {
            builder.append(stringify(response.getPlace()));
        }

        return builder.toString();
    }

    static String stringify(FindCurrentPlaceResponse response, boolean raw) {
        StringBuilder builder = new StringBuilder();

        builder.append(response.getPlaceLikelihoods().size()).append(" Current Place Results:");

        if (raw) {
            builder.append(RESULT_SEPARATOR);
            appendListToStringBuilder(builder, response.getPlaceLikelihoods());
        } else {
            for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                builder
                        .append(RESULT_SEPARATOR)
                        .append("Likelihood: ")
                        .append(placeLikelihood.getLikelihood())
                        .append(FIELD_SEPARATOR)
                        .append("Place: ")
                        .append(stringify(placeLikelihood.getPlace()));
            }
        }

        return builder.toString();
    }

    static String stringify(Place place) {
        return place.getName()
                + FIELD_SEPARATOR+" ("
                + place.getAddress()
                + ")";
    }

    static String stringify(Bitmap bitmap) {
        StringBuilder builder = new StringBuilder();

        builder
                .append("Photo size (width x height)")
                .append(RESULT_SEPARATOR)
                .append(bitmap.getWidth())
                .append(", ")
                .append(bitmap.getHeight());

        return builder.toString();
    }

    public static String stringifyAutocompleteWidget(Place place) {
        StringBuilder builder = new StringBuilder();
        builder.append((stringify(place)));
        return builder.toString();
    }

    private static <T> void appendListToStringBuilder(StringBuilder builder, List<T> items) {
        if (items.isEmpty()) {
            return;
        }

        builder.append(items.get(0));
        for (int i = 1; i < items.size(); i++) {
            builder.append(RESULT_SEPARATOR);
            builder.append(items.get(i));
        }
    }
}
