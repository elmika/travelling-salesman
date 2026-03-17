import type {
  ProblemResponse,
  ProblemTypesResponse,
  SolveRequest,
  SolveResponse,
  ImproveRequest,
  ImproveResponse,
  BenchmarkRequest,
  BenchmarkResponse,
  ApiError,
} from './types';

export class ApiCallError extends Error {
  constructor(
    message: string,
    public readonly status: number
  ) {
    super(message);
    this.name = 'ApiCallError';
  }
}

async function request<T>(url: string, init?: RequestInit): Promise<T> {
  const response = await fetch(url, {
    headers: { 'Content-Type': 'application/json' },
    ...init,
  });

  if (!response.ok) {
    let message = `HTTP ${response.status}`;
    try {
      const body = (await response.json()) as ApiError;
      if (body.error) message = body.error;
    } catch {
      // ignore parse errors
    }
    throw new ApiCallError(message, response.status);
  }

  return response.json() as Promise<T>;
}

export function fetchProblemTypes(): Promise<ProblemTypesResponse> {
  return request<ProblemTypesResponse>('/api/problem-types');
}

export function fetchProblem(type: string): Promise<ProblemResponse> {
  return request<ProblemResponse>(`/api/problems/${encodeURIComponent(type)}`);
}

export function solveProblem(req: SolveRequest): Promise<SolveResponse> {
  return request<SolveResponse>('/api/solve', {
    method: 'POST',
    body: JSON.stringify(req),
  });
}

export function improveSolution(req: ImproveRequest): Promise<ImproveResponse> {
  return request<ImproveResponse>('/api/improve', {
    method: 'POST',
    body: JSON.stringify(req),
  });
}

export function runBenchmark(req: BenchmarkRequest): Promise<BenchmarkResponse> {
  return request<BenchmarkResponse>('/api/benchmark', {
    method: 'POST',
    body: JSON.stringify(req),
  });
}
