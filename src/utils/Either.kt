package net.rocketparty.utils

sealed class Either<out L, out R> {

    inline fun <T> fold(ifLeft: (L) -> T, ifRight: (R) -> T): T {
        return when (this) {
            is Left -> ifLeft(value)
            is Right -> ifRight(value)
        }
    }

    inline fun <N> mapLeft(map: (L) -> N): Either<N, R> {
        return fold({ Left(map(it)) }, { Right(it) })
    }

    inline fun <N> mapRight(map: (R) -> N): Either<L, N> {
        return fold({ Left(it) }, { Right(map(it)) })
    }

    inline fun <NL, NR> flatMap(map: (Either<L, R>) -> Either<NL, NR>): Either<NL, NR> {
        return map(this)
    }

}

class Left<L>(
    val value: L
) : Either<L, Nothing>()

class Right<R>(
    val value: R
) : Either<Nothing, R>()

inline fun <R> recover(from: () -> R): Either<Throwable, R> {
    return try {
        Right(from())
    } catch (e: Throwable) {
        Left(e)
    }
}

inline fun <L, R> retrieve(condition: Boolean, ifFalse: () -> L, ifTrue: () -> R): Either<L, R> {
    return if (condition)
        Right(ifTrue())
    else
        Left(ifFalse())
}

inline fun <L, A> A?.wrap(ifNull: () -> L): Either<L, A> {
    return this?.let { Right(this) } ?: Left(ifNull())
}