import type {
  Point,
  Route,
  ResolutionStrategy,
  ImprovementStrategy,
  BenchmarkEntry,
} from '../api/types';
import { StatusBar } from './StatusBar';

const range = (prefix: string, lo: number, hi: number): string[] =>
  Array.from({ length: hi - lo + 1 }, (_, i) => `${prefix}${lo + i}`);

const BASE_PROBLEM_TYPES = [
  'euclidean',
  ...range('cities', 8, 15),
  ...range('fully-random', 1, 150),
  ...range('partially-random', 1, 150),
];

const RESOLUTION_STRATEGIES: ResolutionStrategy[] = [
  'nearest-neighbor',
  'greedy-edge',
  'random',
  'random10',
  'random100',
  'brute-force',
];

const IMPROVEMENT_STRATEGIES: ImprovementStrategy[] = ['2opt', 'oropt', 'sa', 'uncrossing'];

interface SidebarProps {
  points: Point[];
  problemType: string;
  tsplibTypes: string[];
  currentRoute: Route | null;
  selectedResolutionStrategy: ResolutionStrategy;
  selectedImprovementStrategy: ImprovementStrategy;
  benchmarkEntries: BenchmarkEntry[];
  loading: boolean;
  error: string | null;
  onProblemTypeChange: (type: string) => void;
  onLoadProblem: () => void;
  onResolutionStrategyChange: (strategy: ResolutionStrategy) => void;
  onSolve: () => void;
  onImprovementStrategyChange: (strategy: ImprovementStrategy) => void;
  onImprove: () => void;
  onRunBenchmark: () => void;
}

export function Sidebar({
  points,
  problemType,
  tsplibTypes,
  currentRoute,
  selectedResolutionStrategy,
  selectedImprovementStrategy,
  benchmarkEntries,
  loading,
  error,
  onProblemTypeChange,
  onLoadProblem,
  onResolutionStrategyChange,
  onSolve,
  onImprovementStrategyChange,
  onImprove,
  onRunBenchmark,
}: SidebarProps) {
  return (
    <aside className="sidebar">
      <h1 style={{ fontSize: 18, fontWeight: 700, color: '#1976d2' }}>TSP Solver</h1>

      <section>
        <h2 style={{ fontSize: 12, fontWeight: 600, textTransform: 'uppercase', color: '#757575', marginBottom: 8 }}>
          Problem
        </h2>
        <select
          value={problemType}
          onChange={(e) => onProblemTypeChange(e.target.value)}
          style={{ width: '100%', marginBottom: 8, padding: '6px 8px', fontSize: 13 }}
        >
          <optgroup label="General">
            {BASE_PROBLEM_TYPES.map((t) => (
              <option key={t} value={t}>
                {t}
              </option>
            ))}
          </optgroup>
          {tsplibTypes.length > 0 && (
            <optgroup label="TSPLIB">
              {tsplibTypes.map((t) => (
                <option key={t} value={t}>
                  {t}
                </option>
              ))}
            </optgroup>
          )}
        </select>
        <button
          onClick={onLoadProblem}
          disabled={loading}
          style={{ width: '100%', padding: '7px 0', fontSize: 13, cursor: loading ? 'not-allowed' : 'pointer' }}
        >
          Load
        </button>
      </section>

      <section>
        <h2 style={{ fontSize: 12, fontWeight: 600, textTransform: 'uppercase', color: '#757575', marginBottom: 8 }}>
          Solve
        </h2>
        <select
          value={selectedResolutionStrategy}
          onChange={(e) => onResolutionStrategyChange(e.target.value as ResolutionStrategy)}
          style={{ width: '100%', marginBottom: 8, padding: '6px 8px', fontSize: 13 }}
        >
          {RESOLUTION_STRATEGIES.map((s) => (
            <option key={s} value={s}>
              {s}
            </option>
          ))}
        </select>
        <button
          onClick={onSolve}
          disabled={loading || points.length === 0}
          style={{
            width: '100%',
            padding: '7px 0',
            fontSize: 13,
            cursor: loading || points.length === 0 ? 'not-allowed' : 'pointer',
          }}
        >
          Solve
        </button>
      </section>

      <section>
        <h2 style={{ fontSize: 12, fontWeight: 600, textTransform: 'uppercase', color: '#757575', marginBottom: 8 }}>
          Improve
        </h2>
        <select
          value={selectedImprovementStrategy}
          onChange={(e) => onImprovementStrategyChange(e.target.value as ImprovementStrategy)}
          style={{ width: '100%', marginBottom: 8, padding: '6px 8px', fontSize: 13 }}
        >
          {IMPROVEMENT_STRATEGIES.map((s) => (
            <option key={s} value={s}>
              {s}
            </option>
          ))}
        </select>
        <button
          onClick={onImprove}
          disabled={loading || currentRoute === null}
          style={{
            width: '100%',
            padding: '7px 0',
            fontSize: 13,
            cursor: loading || currentRoute === null ? 'not-allowed' : 'pointer',
          }}
        >
          Improve
        </button>
      </section>

      <section>
        <h2 style={{ fontSize: 12, fontWeight: 600, textTransform: 'uppercase', color: '#757575', marginBottom: 8 }}>
          Benchmark
        </h2>
        <p style={{ fontSize: 13, color: '#616161', marginBottom: 8 }}>
          {benchmarkEntries.length} result{benchmarkEntries.length !== 1 ? 's' : ''} queued
        </p>
        <button
          onClick={onRunBenchmark}
          disabled={loading || benchmarkEntries.length < 2}
          style={{
            width: '100%',
            padding: '7px 0',
            fontSize: 13,
            cursor: loading || benchmarkEntries.length < 2 ? 'not-allowed' : 'pointer',
          }}
        >
          Run Benchmark
        </button>
      </section>

      <div style={{ marginTop: 'auto' }}>
        <StatusBar loading={loading} error={error} />
      </div>
    </aside>
  );
}
