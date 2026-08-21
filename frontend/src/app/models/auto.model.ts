/**
 * Represents an automobile in the system.
 */
export interface Auto {
  /** Unique identifier for the auto (optional). */
  id?: number;
  /** The manufacturer of the auto. */
  brand: string;
  /** The specific model name of the auto. */
  model: string;
  /** The manufacturing year of the auto. */
  year: number;
  /** The license plate number of the auto. */
  licensePlate: string;
  /** The color of the auto. */
  color: string;
}
