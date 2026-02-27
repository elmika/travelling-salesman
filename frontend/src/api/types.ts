export interface Point {
  x: number;
  y: number;
}

export type Route = number[];

export interface Solution {
  route: Route;
  totalDistance: number;
}

export type ResolutionStrategy =
  | 'brute-force'
  | 'random'
  | 'random10'
  | 'random100'
  | 'nearest-neighbor'
  | 'greedy-edge';

export type ImprovementStrategy = '2opt' | 'oropt' | 'sa' | 'uncrossing';

export interface ProblemResponse {
  points: Point[];
}

export interface SolveRequest {
  points: Point[];
  strategy: ResolutionStrategy;
}

export interface SolveResponse {
  route: Route;
  totalDistance: number;
  strategy: string;
  durationMs: number;
}

export interface ImproveRequest {
  points: Point[];
  solution: Solution;
  strategy: ImprovementStrategy;
}

export interface ImproveResponse {
  route: Route;
  totalDistance: number;
  strategy: string;
  originalDistance: number;
  improvementPercent: number;
  durationMs: number;
}

export interface BenchmarkEntry {
  label: string;
  distance: number;
  durationMs: number;
}

export interface BenchmarkRequest {
  solutions: BenchmarkEntry[];
}

export interface BenchmarkResult {
  rank: number;
  label: string;
  distance: number;
  gapPercent: number;
  durationMs: number;
}

export interface BenchmarkResponse {
  results: BenchmarkResult[];
}

export interface ApiError {
  error: string;
}
