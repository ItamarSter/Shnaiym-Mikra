package com.itamarstern.shnaim_mikra.module.aliya

import com.google.gson.annotations.SerializedName


data class AvailableVersions (

  @SerializedName("status"                    ) var status                    : String?  = null,
  @SerializedName("priority"                  ) var priority                  : Int?     = null,
  @SerializedName("license"                   ) var license                   : String?  = null,
  @SerializedName("versionNotes"              ) var versionNotes              : String?  = null,
  @SerializedName("formatAsPoetry"            ) var formatAsPoetry            : String?  = null,
  @SerializedName("digitizedBySefaria"        ) var digitizedBySefaria        : String?  = null,
  @SerializedName("method"                    ) var method                    : String?  = null,
  @SerializedName("heversionSource"           ) var heversionSource           : String?  = null,
  @SerializedName("versionUrl"                ) var versionUrl                : String?  = null,
  @SerializedName("versionTitleInHebrew"      ) var versionTitleInHebrew      : String?  = null,
  @SerializedName("versionNotesInHebrew"      ) var versionNotesInHebrew      : String?  = null,
  @SerializedName("shortVersionTitle"         ) var shortVersionTitle         : String?  = null,
  @SerializedName("shortVersionTitleInHebrew" ) var shortVersionTitleInHebrew : String?  = null,
  @SerializedName("extendedNotes"             ) var extendedNotes             : String?  = null,
  @SerializedName("extendedNotesHebrew"       ) var extendedNotesHebrew       : String?  = null,
  @SerializedName("purchaseInformationImage"  ) var purchaseInformationImage  : String?  = null,
  @SerializedName("purchaseInformationURL"    ) var purchaseInformationURL    : String?  = null,
  @SerializedName("hasManuallyWrappedRefs"    ) var hasManuallyWrappedRefs    : String?  = null,
  @SerializedName("actualLanguage"            ) var actualLanguage            : String?  = null,
  @SerializedName("languageFamilyName"        ) var languageFamilyName        : String?  = null,
  @SerializedName("isBaseText"                ) var isBaseText                : Boolean? = null,
  @SerializedName("isSource"                  ) var isSource                  : Boolean? = null,
  @SerializedName("isPrimary"                 ) var isPrimary                 : String?  = null,
  @SerializedName("direction"                 ) var direction                 : String?  = null,
  @SerializedName("language"                  ) var language                  : String?  = null,
  @SerializedName("title"                     ) var title                     : String?  = null,
  @SerializedName("versionSource"             ) var versionSource             : String?  = null,
  @SerializedName("versionTitle"              ) var versionTitle              : String?  = null

)