package empire.digiprem.mycoloapp.core.data

import empire.digiprem.mycoloapp.core.domain.error.AppErrorCode
import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException


suspend fun <T> safeSupabaseCall(call: suspend () -> T): Result<T, DataError.Remote> {
    return try {
        Result.Success(call())
    } catch (e: NoTransformationFoundException) {
        Result.Failure(DataError.Remote.Serialization)
    }  catch (e: PostgrestRestException) {

        val error = when {

            AppErrorCode.entries.any { it.code== e.code } -> DataError.Remote.InvalidOperation(AppErrorCode.fromCode(e.code!!)!!)
            // ============================================
            // ERREURS BASE DE DONNÉES POSTGRESQL
            // ============================================
            e.code == "42501" -> DataError.Remote.Forbidden          // privilèges insuffisants
            e.code == "23503" -> DataError.Remote.Conflict           // violation FK
            e.code == "23505" -> DataError.Remote.Conflict           // violation unicité
            e.code == "42883" -> DataError.Remote.NotFound           // fonction non définie
            e.code == "42P01" -> DataError.Remote.NotFound           // table non définie
            e.code == "42P17" -> DataError.Remote.ServerError        // récursion infinie
            e.code == "25006" -> DataError.Remote.ReadOnly           // transaction read-only
            e.code?.startsWith("08") == true -> DataError.Remote.Network        // erreur connexion
            e.code?.startsWith("09") == true -> DataError.Remote.ServerError    // exception trigger
            e.code?.startsWith("0L") == true -> DataError.Remote.Forbidden      // donateur invalide
            e.code?.startsWith("0P") == true -> DataError.Remote.Forbidden      // rôle invalide
            e.code?.startsWith("28") == true -> DataError.Remote.Forbidden      // auth invalide
            e.code?.startsWith("40") == true -> DataError.Remote.ServerError    // annulation transaction
            e.code?.startsWith("53") == true -> DataError.Remote.ServerError    // ressources insuffisantes
            e.code?.startsWith("54") == true -> DataError.Remote.ServerError    // trop complexe
            e.code?.startsWith("55") == true -> DataError.Remote.ServerError    // objet mauvais état
            e.code?.startsWith("57") == true -> DataError.Remote.ServerError    // intervention opérateur
            e.code?.startsWith("58") == true -> DataError.Remote.ServerError    // erreur système
            e.code?.startsWith("XX") == true -> DataError.Remote.ServerError    // erreur interne
            e.code?.startsWith("P0") == true -> DataError.Remote.ServerError    // erreur PL/pgSQL

            // ============================================
            // ERREURS API POSTGREST (PGRST)
            // ============================================

            // Connexion
            e.code == "PGRST000" -> DataError.Remote.Network         // impossible connexion BD
            e.code == "PGRST001" -> DataError.Remote.Network         // erreur interne connexion
            e.code == "PGRST002" -> DataError.Remote.Network         // erreur cache schéma
            e.code == "PGRST003" -> DataError.Remote.RequestTimeOut  // timeout pool connexion

            // Requêtes
            e.code == "PGRST100" -> DataError.Remote.BadRequest      // erreur parsing query
            e.code == "PGRST101" -> DataError.Remote.BadRequest      // verbe HTTP non autorisé
            e.code == "PGRST102" -> DataError.Remote.BadRequest      // corps requête invalide
            e.code == "PGRST103" -> DataError.Remote.BadRequest      // plage invalide
            e.code == "PGRST105" -> DataError.Remote.BadRequest      // UPDATE/UPSERT invalide
            e.code == "PGRST106" -> DataError.Remote.BadRequest      // schéma non exposé
            e.code == "PGRST107" -> DataError.Remote.BadRequest      // Content-Type invalide
            e.code == "PGRST108" -> DataError.Remote.BadRequest      // filtre invalide
            e.code == "PGRST111" -> DataError.Remote.ServerError     // response.headers invalide
            e.code == "PGRST112" -> DataError.Remote.ServerError     // code statut invalide
            e.code == "PGRST114" -> DataError.Remote.BadRequest      // UPSERT PUT avec limits
            e.code == "PGRST115" -> DataError.Remote.BadRequest      // UPSERT PUT clé différente
            e.code == "PGRST116" -> DataError.Remote.NotFound        // 0 ou plusieurs résultats
            e.code == "PGRST117" -> DataError.Remote.BadRequest      // verbe HTTP non supporté
            e.code == "PGRST118" -> DataError.Remote.BadRequest      // tri invalide
            e.code == "PGRST120" -> DataError.Remote.BadRequest      // filtre ressource intégrée
            e.code == "PGRST121" -> DataError.Remote.ServerError     // JSON RAISE invalide
            e.code == "PGRST122" -> DataError.Remote.BadRequest      // préférences invalides
            e.code == "PGRST123" -> DataError.Remote.BadRequest      // agrégation désactivée
            e.code == "PGRST124" -> DataError.Remote.BadRequest      // max-affected violé
            e.code == "PGRST125" -> DataError.Remote.NotFound        // chemin URL invalide
            e.code == "PGRST126" -> DataError.Remote.NotFound        // openapi désactivé
            e.code == "PGRST127" -> DataError.Remote.BadRequest      // feature non implémentée
            e.code == "PGRST128" -> DataError.Remote.BadRequest      // max-affected RPC violé

            // Cache schéma
            e.code == "PGRST200" -> DataError.Remote.BadRequest      // FK obsolète
            e.code == "PGRST201" -> DataError.Remote.BadRequest      // intégration ambiguë
            e.code == "PGRST202" -> DataError.Remote.NotFound        // fonction introuvable
            e.code == "PGRST203" -> DataError.Remote.BadRequest      // fonctions surchargées
            e.code == "PGRST204" -> DataError.Remote.BadRequest      // colonne introuvable
            e.code == "PGRST205" -> DataError.Remote.NotFound        // table introuvable

            // Authentification
            e.code == "PGRST300" -> DataError.Remote.ServerError     // pas de secret JWT
            e.code == "PGRST301" -> DataError.Remote.Unauthorized    // JWT invalide
            e.code == "PGRST302" -> DataError.Remote.Unauthorized    // pas de Bearer
            e.code == "PGRST303" -> DataError.Remote.Unauthorized    // JWT claims invalide

            // Interne
            e.code?.startsWith("PGRSTX") == true -> DataError.Remote.ServerError

            // ============================================
            // FALLBACK — HTTP STATUS
            // ============================================
            else -> when (e.response.status.value) {
                400 -> DataError.Remote.BadRequest
                401 -> DataError.Remote.Unauthorized
                403 -> DataError.Remote.Forbidden
                404 -> DataError.Remote.NotFound
                405 -> DataError.Remote.BadRequest
                406 -> DataError.Remote.BadRequest
                409 -> DataError.Remote.Conflict
                415 -> DataError.Remote.BadRequest
                416 -> DataError.Remote.BadRequest
                500 -> DataError.Remote.ServerError
                503 -> DataError.Remote.Network
                504 -> DataError.Remote.RequestTimeOut

                else -> {
                    println(">>> PostgrestRestException")
                    println(">>> code    : ${e.code}")
                    println(">>> status  : ${e.response.status.value}")
                    println(">>> message : ${e.message}")
                    println(">>> hint    : ${e.hint}")
                    println(">>> details : ${e.details}")
                    DataError.Remote.Unknown
                }
            }
        }
        return Result.Failure(error)
    } catch (e: SerializationException) {
        Result.Failure(DataError.Remote.Serialization)
    } catch (e: HttpRequestTimeoutException) {
        Result.Failure(DataError.Remote.RequestTimeOut)
    } catch (e: CancellationException) {
        Result.Failure(DataError.Remote.RequestTimeOut)
    }  catch (e: CancellationException) {
        Result.Failure(DataError.Remote.RequestTimeOut)
    } catch (e: Exception) {
        val message = e.message.orEmpty()



        // ✅ Cas 3 — Fallback string matching
        val error = when {
            message.contains("23505") ||
                    message.contains("unique", ignoreCase = true) ||
                    message.contains("duplicate", ignoreCase = true) ||
                    message.contains("409") -> DataError.Remote.Conflict

            message.contains("404") ||
                    message.contains("not found", ignoreCase = true) -> DataError.Remote.NotFound

            message.contains("401") ||
                    message.contains("400") ||
                    message.contains("Unauthorized", ignoreCase = true) ||
                    message.contains("Invalid login", ignoreCase = true) ||
                    message.contains("invalid_grant", ignoreCase = true) -> DataError.Remote.Unauthorized

            message.contains("500") ||
                    message.contains("server", ignoreCase = true) -> DataError.Remote.ServerError

            message.contains("timeout", ignoreCase = true) -> DataError.Remote.RequestTimeOut

            message.contains("network", ignoreCase = true) ||
                    message.contains("connect", ignoreCase = true) ||
                    message.contains("Failed to fetch", ignoreCase = true) ||
                    message.contains("Fail to fetch", ignoreCase = true) ||
                    message.contains("NetworkError", ignoreCase = true) ||
                    message.contains("Load failed", ignoreCase = true) -> DataError.Remote.Network

            else -> {
                println(">>> unknown error: ${e::class} — $message")
                DataError.Remote.Unknown
            }
        }
        Result.Failure(error)
    }
}
