package com.gentlemonster.GentleMonsterBE.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum StoreEnum {
    VIET_NAM("vn"),
    KOREA("kr"),
    AMERICA("us"),
    BRAZIL("bz"),
    JAPAN("jp"),
    THAILAND("th"),
    MALAYSIA("my"),
    SINGAPORE("sg"),
    PHILIPPINES("ph"),
    INDONESIA("ids"),
    HONG_KONG("hk"),
    UNITED_KINGDOM("uk"),
    FRANCE("fr"),
    GERMANY("de"),
    ITALY("it"),
    CANADA("ca"),
    AUSTRALIA("au"),
    NEW_ZEALAND("nz"),
    TAIWAN("tw"),
    SWITZERLAND("ch"),
    NETHERLANDS("nl"),
    BELGIUM("be"),
    SWEDEN("se"),
    NORWAY("no"),
    DENMARK("dk"),
    FINLAND("fi"),
    IRELAND("ie"),
    AUSTRIA("at"),
    POLAND("pl"),
    CZECH_REPUBLIC("cz"),
    HUNGARY("hu"),
    PORTUGAL("pt");

    private String code;

    public static String getCodeByCountry(String country){
        try {
            String countryEnum = country.toUpperCase().replace(" ", "_");
            return StoreEnum.valueOf(countryEnum).getCode();
        }catch (IllegalArgumentException e) {
            return "unknown";
        }
    }
}
