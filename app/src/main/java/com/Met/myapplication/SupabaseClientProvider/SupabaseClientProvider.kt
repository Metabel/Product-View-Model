package supabaseClientProvider

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClientProvider  {
    val supabase: SupabaseClient= createSupabaseClient(
        supabaseUrl ="https://mvuiwjzxssqxlpqqfltq.supabase.co",
        supabaseKey="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im12dWl3anp4c3NxeGxwcXFmbHRxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTQ5ODg5NzAsImV4cCI6MjA3MDU2NDk3MH0.4Dyc7wbiV2H87-15QIXXIxeS7dStnROwgXHxqTh0k-Q"

    ){
        install(Postgrest)
        install(Storage)
        }
}
