/**
 * Represents a user in the system.
 */
export interface User {
  /** Unique identifier for the user (optional). */
  id?: number;
  /** The user's login name. */
  username: string;
  /** The user's password (optional, typically used during authentication). */
  password?: string;
}
