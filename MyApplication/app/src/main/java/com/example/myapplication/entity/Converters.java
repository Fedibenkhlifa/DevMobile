package com.example.myapplication.entity;

import androidx.room.TypeConverter;
import java.util.Date;

public class Converters {

    // Convert Role to/from String
    @TypeConverter
    public static Role fromRoleString(String value) {
        return Role.valueOf(value);
    }

    @TypeConverter
    public static String toRoleString(Role role) {
        return role.name();
    }

    // Convert ServiceCategory to/from String
    @TypeConverter
    public static ServiceCategory fromCategoryString(String value) {
        return ServiceCategory.valueOf(value);
    }

    @TypeConverter
    public static String toCategoryString(ServiceCategory category) {
        return category.name();
    }

    // Convert Date to/from Long (for reservationDate)
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
