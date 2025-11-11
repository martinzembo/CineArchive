package edu.utn.inspt.cinearchive.backend.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilidad para gestionar contraseñas de forma segura
 * Usa BCrypt para hashear y verificar contraseñas
 *
 * BCrypt es un algoritmo de hash adaptativo que:
 * - Añade "salt" automáticamente (previene rainbow tables)
 * - Es lento por diseño (previene ataques de fuerza bruta)
 * - Genera un hash diferente cada vez (aunque la contraseña sea la misma)
 */
public class PasswordUtil {

    /**
     * Factor de trabajo de BCrypt (número de rondas)
     * 12 es un buen balance entre seguridad y rendimiento
     * Cada incremento duplica el tiempo de procesamiento
     */
    private static final int BCRYPT_ROUNDS = 12;

    /**
     * Encripta (hashea) una contraseña en texto plano usando BCrypt
     *
     * @param passwordPlano Contraseña en texto plano ingresada por el usuario
     * @return Hash BCrypt de la contraseña (60 caracteres)
     * @throws IllegalArgumentException si la contraseña es null o vacía
     *
     * @example
     * String hash = PasswordUtil.encriptar("MiPassword123");
     * // Retorna: "$2a$12$K5v7..."
     */
    public static String encriptar(String passwordPlano) {
        if (passwordPlano == null || passwordPlano.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        // BCrypt.hashpw genera un hash único cada vez
        // Incluye el salt automáticamente en el resultado
        return BCrypt.hashpw(passwordPlano, BCrypt.gensalt(BCRYPT_ROUNDS));
    }

    /**
     * Verifica si una contraseña en texto plano coincide con un hash BCrypt
     *
     * @param passwordPlano Contraseña en texto plano ingresada por el usuario
     * @param passwordHash Hash BCrypt almacenado en la base de datos
     * @return true si la contraseña es correcta, false si no coincide
     *
     * @example
     * String hashGuardado = "$2a$12$K5v7..."; // De la BD
     * boolean esCorrecta = PasswordUtil.verificar("MiPassword123", hashGuardado);
     * if (esCorrecta) {
     *     // Login exitoso
     * }
     */
    public static boolean verificar(String passwordPlano, String passwordHash) {
        if (passwordPlano == null || passwordHash == null) {
            return false;
        }

        try {
            // BCrypt.checkpw compara de forma segura
            // Funciona aunque el hash se haya generado con diferente salt
            return BCrypt.checkpw(passwordPlano, passwordHash);
        } catch (IllegalArgumentException e) {
            // El hash no es válido o está corrupto
            return false;
        }
    }

    /**
     * Valida que una contraseña cumpla con los requisitos mínimos de seguridad
     *
     * Requisitos:
     * - Mínimo 8 caracteres
     * - Al menos una letra mayúscula
     * - Al menos una letra minúscula
     * - Al menos un número
     *
     * @param password Contraseña a validar
     * @return true si cumple los requisitos, false si no
     *
     * @example
     * PasswordUtil.esSegura("Password1"); // true
     * PasswordUtil.esSegura("password");  // false (falta mayúscula y número)
     * PasswordUtil.esSegura("PASS1");     // false (menos de 8 caracteres)
     */
    public static boolean esSegura(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        // Verificar que tenga al menos una mayúscula
        boolean tieneMayuscula = password.matches(".*[A-Z].*");

        // Verificar que tenga al menos una minúscula
        boolean tieneMinuscula = password.matches(".*[a-z].*");

        // Verificar que tenga al menos un número
        boolean tieneNumero = password.matches(".*\\d.*");

        return tieneMayuscula && tieneMinuscula && tieneNumero;
    }

    /**
     * Valida que una contraseña cumpla con requisitos FUERTES de seguridad
     *
     * Requisitos:
     * - Mínimo 10 caracteres
     * - Al menos una letra mayúscula
     * - Al menos una letra minúscula
     * - Al menos un número
     * - Al menos un carácter especial (!@#$%^&*()_+-=[]{}|;:,.<>?)
     *
     * @param password Contraseña a validar
     * @return true si cumple los requisitos fuertes, false si no
     *
     * @example
     * PasswordUtil.esMuySegura("Password1!");     // true
     * PasswordUtil.esMuySegura("Password1");      // false (falta caracter especial)
     * PasswordUtil.esMuySegura("Pass1!");         // false (menos de 10 caracteres)
     */
    public static boolean esMuySegura(String password) {
        if (password == null || password.length() < 10) {
            return false;
        }

        boolean tieneMayuscula = password.matches(".*[A-Z].*");
        boolean tieneMinuscula = password.matches(".*[a-z].*");
        boolean tieneNumero = password.matches(".*\\d.*");

        // Verificar que tenga al menos un carácter especial
        boolean tieneEspecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{}|;:,.<>?].*");

        return tieneMayuscula && tieneMinuscula && tieneNumero && tieneEspecial;
    }

    /**
     * Genera un mensaje descriptivo de los requisitos que no cumple una contraseña
     * Útil para mostrar feedback al usuario durante el registro
     *
     * @param password Contraseña a validar
     * @return Mensaje descriptivo de lo que falta, o cadena vacía si es válida
     *
     * @example
     * String mensaje = PasswordUtil.obtenerMensajeValidacion("pass");
     * // Retorna: "La contraseña debe tener al menos 8 caracteres. Debe contener al menos una mayúscula..."
     */
    public static String obtenerMensajeValidacion(String password) {
        if (password == null || password.isEmpty()) {
            return "La contraseña es obligatoria";
        }

        StringBuilder mensaje = new StringBuilder();

        if (password.length() < 8) {
            mensaje.append("La contraseña debe tener al menos 8 caracteres. ");
        }

        if (!password.matches(".*[A-Z].*")) {
            mensaje.append("Debe contener al menos una letra mayúscula. ");
        }

        if (!password.matches(".*[a-z].*")) {
            mensaje.append("Debe contener al menos una letra minúscula. ");
        }

        if (!password.matches(".*\\d.*")) {
            mensaje.append("Debe contener al menos un número. ");
        }

        return mensaje.toString().trim();
    }

    /**
     * Verifica si un hash de contraseña necesita ser regenerado
     * Útil cuando se aumenta el factor de trabajo de BCrypt
     *
     * @param passwordHash Hash existente
     * @return true si necesita regenerarse, false si está actualizado
     */
    public static boolean necesitaRegenerar(String passwordHash) {
        if (passwordHash == null || passwordHash.length() < 7) {
            return true;
        }

        try {
            // Extraer el factor de trabajo del hash
            // Formato BCrypt: $2a$12$... (12 es el factor de trabajo)
            String[] partes = passwordHash.split("\\$");
            if (partes.length < 3) {
                return true;
            }

            int factorActual = Integer.parseInt(partes[2]);
            return factorActual < BCRYPT_ROUNDS;

        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Método auxiliar para testing - NO usar en producción
     * Genera un hash rápido con menos rondas (solo para pruebas)
     *
     * @param passwordPlano Contraseña en texto plano
     * @return Hash BCrypt con factor de trabajo reducido
     */
    @Deprecated
    public static String encriptarRapido(String passwordPlano) {
        if (passwordPlano == null || passwordPlano.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        // Solo 4 rondas para testing (no seguro para producción)
        return BCrypt.hashpw(passwordPlano, BCrypt.gensalt(4));
    }
}

