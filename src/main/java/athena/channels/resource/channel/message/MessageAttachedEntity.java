package athena.channels.resource.channel.message;

import athena.channels.resource.channel.message.entity.EntityMediaType;

import java.util.Map;

/**
 * An entity meant to be attached to a message.
 *
 * @author Vrekt, Armisto
 */
public final class MessageAttachedEntity {

    /**
     * The tag?
     * The title of this entity?
     * The description of this entity?
     * The content type, not sure why this is here also since we have {@code type}
     * The file-name.
     */
    private String tag, title, description, contentType, filename;
    /**
     * The media type.
     */
    private EntityMediaType type;
    /**
     * List of metadata associated with this entity.
     */
    private Map<String, String> metadata;

    /**
     * Initialize this entity.
     *
     * @param tag         the tag
     * @param title       the title
     * @param description the description
     * @param contentType the content-type
     * @param filename    the file-name
     * @param type        the media-type
     * @param metadata    the metadata.
     */
    public MessageAttachedEntity(String tag, String title, String description, String contentType, String filename, EntityMediaType type, Map<String, String> metadata) {
        this.tag = tag;
        this.title = title;
        this.description = description;
        this.contentType = contentType;
        this.filename = filename;
        this.type = type;
        this.metadata = metadata;
    }
}
