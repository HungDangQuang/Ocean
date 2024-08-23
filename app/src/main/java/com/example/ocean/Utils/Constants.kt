package com.example.ocean.Utils

object Constants {
    const val DEFAULT_INPUT_COUNTRY = "English"
    const val DEFAULT_OUTPUT_COUNTRY = "Vietnamese"
    val supportedLanguageList = listOf(
        CountryLanguage("ZA", "Afrikaans", "af"),
        CountryLanguage("AL", "Albanian", "sq"),
        CountryLanguage("ET", "Amharic", "am"),
        CountryLanguage("SA", "Arabic", "ar"),
        CountryLanguage("AM", "Armenian", "hy"),
        CountryLanguage("AZ", "Azerbaijani", "az"),
        CountryLanguage("ES", "Basque", "eu"),
        CountryLanguage("BY", "Belarusian", "be"),
        CountryLanguage("BD", "Bengali", "bn"),
        CountryLanguage("BA", "Bosnian", "bs"),
        CountryLanguage("BG", "Bulgarian", "bg"),
        CountryLanguage("ES", "Catalan", "ca"),
        CountryLanguage("PH", "Cebuano", "ceb"),
        CountryLanguage("MW", "Chichewa", "ny"),
        CountryLanguage("CN", "Chinese (Simplified)", "zh"),
        CountryLanguage("TW", "Chinese (Traditional)", "zh-TW"),
        CountryLanguage("FR", "Corsican", "co"),
        CountryLanguage("HR", "Croatian", "hr"),
        CountryLanguage("CZ", "Czech", "cs"),
        CountryLanguage("DK", "Danish", "da"),
        CountryLanguage("NL", "Dutch", "nl"),
        CountryLanguage("US", "English", "en"),
        CountryLanguage("EO", "Esperanto", "eo"),
        CountryLanguage("EE", "Estonian", "et"),
        CountryLanguage("PH", "Filipino", "fil"),
        CountryLanguage("FI", "Finnish", "fi"),
        CountryLanguage("FR", "French", "fr"),
        CountryLanguage("NL", "Frisian", "fy"),
        CountryLanguage("ES", "Galician", "gl"),
        CountryLanguage("GE", "Georgian", "ka"),
        CountryLanguage("DE", "German", "de"),
        CountryLanguage("GR", "Greek", "el"),
        CountryLanguage("IN", "Gujarati", "gu"),
        CountryLanguage("HT", "Haitian Creole", "ht"),
        CountryLanguage("NG", "Hausa", "ha"),
        CountryLanguage("US", "Hawaiian", "haw"),
        CountryLanguage("IL", "Hebrew", "he"),
        CountryLanguage("IN", "Hindi", "hi"),
        CountryLanguage("CN", "Hmong", "hmn"),
        CountryLanguage("HU", "Hungarian", "hu"),
        CountryLanguage("IS", "Icelandic", "is"),
        CountryLanguage("NG", "Igbo", "ig"),
        CountryLanguage("ID", "Indonesian", "id"),
        CountryLanguage("IE", "Irish", "ga"),
        CountryLanguage("IT", "Italian", "it"),
        CountryLanguage("JP", "Japanese", "ja"),
        CountryLanguage("ID", "Javanese", "jv"),
        CountryLanguage("IN", "Kannada", "kn"),
        CountryLanguage("KZ", "Kazakh", "kk"),
        CountryLanguage("KH", "Khmer", "km"),
        CountryLanguage("RW", "Kinyarwanda", "rw"),
        CountryLanguage("KR", "Korean", "ko"),
        CountryLanguage("IQ", "Kurdish (Kurmanji)", "ku"),
        CountryLanguage("KG", "Kyrgyz", "ky"),
        CountryLanguage("LA", "Lao", "lo"),
        CountryLanguage("VA", "Latin", "la"),
        CountryLanguage("LV", "Latvian", "lv"),
        CountryLanguage("LT", "Lithuanian", "lt"),
        CountryLanguage("LU", "Luxembourgish", "lb"),
        CountryLanguage("MK", "Macedonian", "mk"),
        CountryLanguage("MG", "Malagasy", "mg"),
        CountryLanguage("MY", "Malay", "ms"),
        CountryLanguage("IN", "Malayalam", "ml"),
        CountryLanguage("MT", "Maltese", "mt"),
        CountryLanguage("NZ", "Maori", "mi"),
        CountryLanguage("IN", "Marathi", "mr"),
        CountryLanguage("MN", "Mongolian", "mn"),
        CountryLanguage("MM", "Myanmar (Burmese)", "my"),
        CountryLanguage("NP", "Nepali", "ne"),
        CountryLanguage("NO", "Norwegian", "no"),
        CountryLanguage("IN", "Odia (Oriya)", "or"),
        CountryLanguage("AF", "Pashto", "ps"),
        CountryLanguage("IR", "Persian", "fa"),
        CountryLanguage("PL", "Polish", "pl"),
        CountryLanguage("PT", "Portuguese", "pt"),
        CountryLanguage("BR", "Portuguese", "pt"),
        CountryLanguage("IN", "Punjabi", "pa"),
        CountryLanguage("RO", "Romanian", "ro"),
        CountryLanguage("RU", "Russian", "ru"),
        CountryLanguage("WS", "Samoan", "sm"),
        CountryLanguage("GB", "Scots Gaelic", "gd"),
        CountryLanguage("RS", "Serbian", "sr"),
        CountryLanguage("LS", "Sesotho", "st"),
        CountryLanguage("ZW", "Shona", "sn"),
        CountryLanguage("PK", "Sindhi", "sd"),
        CountryLanguage("LK", "Sinhala", "si"),
        CountryLanguage("SK", "Slovak", "sk"),
        CountryLanguage("SI", "Slovenian", "sl"),
        CountryLanguage("SO", "Somali", "so"),
        CountryLanguage("ES", "Spanish", "es"),
        CountryLanguage("ID", "Sundanese", "su"),
        CountryLanguage("KE", "Swahili", "sw"),
        CountryLanguage("SE", "Swedish", "sv"),
        CountryLanguage("TJ", "Tajik", "tg"),
        CountryLanguage("IN", "Tamil", "ta"),
        CountryLanguage("RU", "Tatar", "tt"),
        CountryLanguage("IN", "Telugu", "te"),
        CountryLanguage("TH", "Thai", "th"),
        CountryLanguage("TR", "Turkish", "tr"),
        CountryLanguage("TM", "Turkmen", "tk"),
        CountryLanguage("UA", "Ukrainian", "uk"),
        CountryLanguage("PK", "Urdu", "ur"),
        CountryLanguage("CN", "Uyghur", "ug"),
        CountryLanguage("UZ", "Uzbek", "uz"),
        CountryLanguage("VN", "Vietnamese", "vi"),
        CountryLanguage("GB", "Welsh", "cy"),
        CountryLanguage("ZA", "Xhosa", "xh"),
        CountryLanguage("IL", "Yiddish", "yi"),
        CountryLanguage("NG", "Yoruba", "yo"),
        CountryLanguage("ZA", "Zulu", "zu")
    )

    // processed image size to the text recognition MLKit
    const val PROCESSED_IMAGE_WIDTH_SIZE = 720
    const val PROCESSED_IMAGE_HEIGHT_SIZE = 1280
}