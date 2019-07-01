package org.frienitto.manitto.domain

import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "rooms")
class Room private constructor(
        owner: User,
        title: String,
        code: String,
        url: String = "",
        expiresDate: LocalDate
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        private set(value) {
            field = value
        }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", foreignKey = ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    var owner: User = owner
        private set(value) {
            field = value
        }
    var title: String = title
        private set(value) {
            field = value
        }
    var code: String = code
        private set(value) {
            field = value
        }
    var url: String = url
        private set(value) {
            field = value
        }
    var expiresDate: LocalDate = expiresDate
        private set(value) {
            field = value
        }
    lateinit var createdAt: LocalDateTime
    var createdBy: String? = null
        private set(value) {
            field = value
        }
    lateinit var updatedAt: LocalDateTime
    var updatedBy: String? = null
        private set(value) {
            field = value
        }

    @PrePersist
    fun onPersist() {
        val now = LocalDateTime.now()
        this.createdAt = now
        this.createdBy = this.createdBy ?: "system"
        this.updatedAt = now
        this.updatedBy = this.updatedBy ?: "system"
    }

    companion object {
        fun newRoom(owner: User, title: String, code: String, expiresDate: LocalDate): Room {
            return Room(owner = owner, title = title, code = code, expiresDate = expiresDate).apply {
                this.createdBy = owner.username
                this.updatedBy = owner.username
                this.expiresDate = expiresDate
            }
        }
    }
}