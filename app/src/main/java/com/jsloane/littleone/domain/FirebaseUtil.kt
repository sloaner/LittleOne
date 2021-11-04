package com.jsloane.littleone.domain

object FirestoreCollection {
    object Users : Collection() {
        override val id = "user"

        enum class Field {
            family
        }
    }

    object Family : Collection() {
        override val id = "family"

        enum class Field {
            users,
            inviteCode,
            inviteExpiration
        }
    }

    object Child : Collection() {
        override val id = "child"

        enum class Field {
            firstName,
            birthday
        }
    }

    object Activity : Collection() {
        override val id = "activity"

        enum class Field {
            type,
            start_time,
            duration,
            notes
        }
    }

    abstract class Collection {
        abstract val id: String
        override fun toString() = id
    }
}
