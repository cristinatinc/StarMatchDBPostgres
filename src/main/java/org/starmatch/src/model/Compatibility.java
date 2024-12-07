package org.starmatch.src.model;

/**
 * Represents the compatibility between two users, identified by their user IDs and a compatibility score.
 */
public class Compatibility {
    private Integer userId;
    private Integer friendId;
    private long compatibilityScore;

    /**
     * Constructs a Compatibility instance with the specified compatibility score, friend ID, and user ID.
     *
     * @param compatibilityScore the compatibility score between the user and friend
     * @param friendId           the ID of the friend user
     * @param userId             the ID of the primary user
     */
    public Compatibility(long compatibilityScore, Integer friendId, Integer userId) {
        this.compatibilityScore = compatibilityScore;
        this.friendId = friendId;
        this.userId = userId;
    }

    /**
     * Gets the compatibility score between the two users.
     *
     * @return the compatibility score
     */
    public long getCompatibilityScore() {
        return compatibilityScore;
    }

    /**
     * Sets the compatibility score between the two users.
     *
     * @param compatibilityScore the new compatibility score
     */
    public void setCompatibilityScore(long compatibilityScore) {
        this.compatibilityScore = compatibilityScore;
    }

    /**
     * Gets the friend user ID.
     *
     * @return the friend ID
     */
    public Integer getFriendId() {
        return friendId;
    }

    /**
     * Sets the friend user ID.
     *
     * @param friendId the new friend ID
     */
    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }

    /**
     * Gets the primary user ID.
     *
     * @return the user ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Sets the primary user ID.
     *
     * @param userId the new user ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Returns a string representation of the compatibility, including user IDs and score.
     *
     * @return a string representation of the compatibility
     */
    @Override
    public String toString() {
        return "Compatibility{" +
                "userId=" + userId +
                ", friendId=" + friendId +
                ", compatibilityScore=" + compatibilityScore +
                '}';
    }
}
