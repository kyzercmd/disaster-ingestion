export interface DisasterEvent {
  eventId: string;
  hazardType: 'flood' | 'cyclone' | 'drought' | 'river_erosion';
  startDate: string;
  endDate: string;
  division: string;
  district: string;
  upazila: string;
  affectedPeople: number;
  deaths: number;
  economicLossBdt: number;
  sourceUrl: string;
  economicLossUsd?: number;
  durationDays?: number;
  severityCategory?: 'low' | 'medium' | 'high' | 'extreme';
  notes?: string;
}

export interface UploadResult {
  accepted: number;
  rejected: number;
  errors: string[];
}

export interface Summary {
  total: number;
  byHazardType: Record<string, number>;
  byDivision: Record<string, number>;
  byYear: Record<string, number>;
}
