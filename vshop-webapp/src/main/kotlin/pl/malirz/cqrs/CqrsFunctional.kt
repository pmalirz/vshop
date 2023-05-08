package pl.malirz.cqrs

import java.util.function.Consumer
import java.util.function.Function

/**
 * Query always has an Answer [A].
 */
interface Query<A : Any>

interface QueryHandler<T : Query<R>, R : Any> : Function<T, R>

interface CommandHandler<T : Any> : Consumer<T>
