package com.ufpb.meuguia.model

data class Attraction(
    val id: String,
    val name: String,
    val description: String,
    val map_link: String,
    val city: String,
    val state: String,
    val image_link: String,
    val fonte: String,
    val segmentations: List<TurismSegmentation>,
    val attractionTypes: AttractionType,
    val moreInfoLinkList: List<MoreInfoLink>
)