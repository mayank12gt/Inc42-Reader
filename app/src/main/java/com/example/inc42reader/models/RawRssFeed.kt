package com.example.inc42reader.models



import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
data class RawRssFeed @JvmOverloads constructor(
    @field:Element(name = "channel")
    var channel: Channel? = null
)

@Root(name = "channel", strict = false)
data class Channel @JvmOverloads constructor(
    @field:Element(name = "title")
    var title: String? = null,



    @field:Element(name = "description")
    var description: String? = null,

    @field:Element(name = "lastBuildDate")
    var lastBuildDate: String? = null,

    @field:Element(name = "language")
    var language: String? = null,

    @field:Element(name = "updatePeriod", required = false)
    @Path("sy:updatePeriod")
    var updatePeriod: String? = null,

    @field:Element(name = "updateFrequency", required = false)
    @Path("sy:updateFrequency")
    var updateFrequency: String? = null,

    @field:Element(name = "generator")
    var generator: String? = null,

    @field:Element(name = "image")
    var image: Image? = null,

    @field:ElementList(inline = true, name = "item")
    var items: List<Item>? = null
)

@Root(name = "image", strict = false)
data class Image @JvmOverloads constructor(
    @field:Element(name = "url")
    var url: String? = null,

    @field:Element(name = "title")
    var title: String? = null,

    @field:Element(name = "link")
    var link: String? = null,

    @field:Element(name = "width", required = false)
    var width: Int? = null,

    @field:Element(name = "height", required = false)
    var height: Int? = null
)

@Root(name = "item", strict = false)
data class Item @JvmOverloads constructor(
    @field:Element(name = "title")
    var title: String? = null,

    @field:Element(name = "link")
    var link: String? = null,

    @field:Element(name = "creator", required = false)
    @Namespace(prefix = "dc")
    var creator: String? = null,

    @field:Element(name = "pubDate")
    var pubDate: String? = null,

    @field:ElementList(inline = true, required = false)
    var category: List<String>? = null,

    @field:Element(name = "guid")
    var guid: String? = null,

    @field:Element(name = "description", required = false)
    var description: String? = null,

    @field:Element(name = "encoded", required = false)
    @Namespace(prefix = "content")
    var encoded: String? = null
)

