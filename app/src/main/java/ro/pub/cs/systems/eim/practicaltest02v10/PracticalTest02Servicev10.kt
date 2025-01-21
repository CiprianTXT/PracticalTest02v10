package ro.pub.cs.systems.eim.practicaltest02v10

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

data class Pokemon(
    @SerializedName("abilities") val abilities: List<AbilityInfo>,
    @SerializedName("base_experience") val baseExperience: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("held_items") val heldItems: List<HeldItem>,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("species") val species: NamedAPIResource,
    @SerializedName("sprites") val sprites: Sprites,
    @SerializedName("stats") val stats: List<StatInfo>,
    @SerializedName("types") val types: List<TypeInfo>,
    @SerializedName("weight") val weight: Int
)

data class AbilityInfo(
    @SerializedName("ability") val ability: NamedAPIResource,
    @SerializedName("is_hidden") val isHidden: Boolean,
    @SerializedName("slot") val slot: Int
)

data class NamedAPIResource(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

data class HeldItem(
    @SerializedName("item") val item: NamedAPIResource,
    @SerializedName("version_details") val versionDetails: List<VersionDetail>
)

data class VersionDetail(
    @SerializedName("rarity") val rarity: Int,
    @SerializedName("version") val version: NamedAPIResource
)

data class Sprites(
    @SerializedName("front_default") val frontDefault: String,
    @SerializedName("back_default") val backDefault: String
)

data class StatInfo(
    @SerializedName("base_stat") val baseStat: Int,
    @SerializedName("effort") val effort: Int,
    @SerializedName("stat") val stat: NamedAPIResource
)

data class TypeInfo(
    @SerializedName("slot") val slot: Int,
    @SerializedName("type") val type: NamedAPIResource
)


interface PracticalTest02Servicev10 {
    @GET("api/v2/pokemon/{pokemonInput}")
    suspend fun getPokemonInfo(
        @Path("pokemonInput") pokemonInput: String
    ): Response<Pokemon>
}