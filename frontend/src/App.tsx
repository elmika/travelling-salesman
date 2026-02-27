import { useState } from 'react';
import type {
  Point,
  Route,
  ResolutionStrategy,
  ImprovementStrategy,
  BenchmarkEntry,
  BenchmarkResult,
} from './api/types';
import { fetchProblem, solveProblem, improveSolution, runBenchmark } from './api/client';
import { Sidebar } from './components/Sidebar';
import { RouteCanvas } from './components/RouteCanvas';
import { BenchmarkTable } from './components/BenchmarkTable';

function uniqueLabel(label: string, existing: BenchmarkEntry[]): string {
  const taken = new Set(existing.map((e) => e.label));
  if (!taken.has(label)) return label;
  let n = 2;
  while (taken.has(`${label} (${n})`)) n++;
  return `${label} (${n})`;
}

export default function App() {
  const [points, setPoints] = useState<Point[]>([]);
  const [problemType, setProblemType] = useState('cities10');
  const [currentRoute, setCurrentRoute] = useState<Route | null>(null);
  const [currentDistance, setCurrentDistance] = useState<number>(0);
  const [currentLabel, setCurrentLabel] = useState<string>('');
  const [currentDurationMs, setCurrentDurationMs] = useState<number>(0);
  const [selectedResolutionStrategy, setSelectedResolutionStrategy] =
    useState<ResolutionStrategy>('nearest-neighbor');
  const [selectedImprovementStrategy, setSelectedImprovementStrategy] =
    useState<ImprovementStrategy>('2opt');
  const [benchmarkEntries, setBenchmarkEntries] = useState<BenchmarkEntry[]>([]);
  const [benchmarkResults, setBenchmarkResults] = useState<BenchmarkResult[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleLoadProblem() {
    setLoading(true);
    setError(null);
    try {
      const res = await fetchProblem(problemType);
      setPoints(res.points);
      setCurrentRoute(null);
      setCurrentDistance(0);
      setCurrentLabel('');
      setCurrentDurationMs(0);
    } catch (e) {
      setError(e instanceof Error ? e.message : String(e));
    } finally {
      setLoading(false);
    }
  }

  async function handleSolve() {
    setLoading(true);
    setError(null);
    try {
      const res = await solveProblem({ points, strategy: selectedResolutionStrategy });
      setCurrentRoute(res.route);
      setCurrentDistance(res.totalDistance);
      setCurrentLabel(selectedResolutionStrategy);
      setCurrentDurationMs(res.durationMs);
      const label = uniqueLabel(selectedResolutionStrategy, benchmarkEntries);
      setBenchmarkEntries((prev) => [
        ...prev,
        { label, distance: res.totalDistance, durationMs: res.durationMs },
      ]);
    } catch (e) {
      setError(e instanceof Error ? e.message : String(e));
    } finally {
      setLoading(false);
    }
  }

  async function handleImprove() {
    if (!currentRoute) return;
    setLoading(true);
    setError(null);
    try {
      const res = await improveSolution({
        points,
        solution: { route: currentRoute, totalDistance: currentDistance },
        strategy: selectedImprovementStrategy,
      });
      const nextLabel = `${currentLabel} → ${selectedImprovementStrategy}`;
      const totalDurationMs = currentDurationMs + res.durationMs;
      setCurrentRoute(res.route);
      setCurrentDistance(res.totalDistance);
      setCurrentLabel(nextLabel);
      setCurrentDurationMs(totalDurationMs);
      const label = uniqueLabel(nextLabel, benchmarkEntries);
      setBenchmarkEntries((prev) => [
        ...prev,
        { label, distance: res.totalDistance, durationMs: totalDurationMs },
      ]);
    } catch (e) {
      setError(e instanceof Error ? e.message : String(e));
    } finally {
      setLoading(false);
    }
  }

  async function handleRunBenchmark() {
    setLoading(true);
    setError(null);
    try {
      const res = await runBenchmark({ solutions: benchmarkEntries });
      setBenchmarkResults(res.results);
    } catch (e) {
      setError(e instanceof Error ? e.message : String(e));
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="root-layout" style={{ display: 'contents' }}>
      <Sidebar
        points={points}
        problemType={problemType}
        currentRoute={currentRoute}
        selectedResolutionStrategy={selectedResolutionStrategy}
        selectedImprovementStrategy={selectedImprovementStrategy}
        benchmarkEntries={benchmarkEntries}
        loading={loading}
        error={error}
        onProblemTypeChange={setProblemType}
        onLoadProblem={handleLoadProblem}
        onResolutionStrategyChange={setSelectedResolutionStrategy}
        onSolve={handleSolve}
        onImprovementStrategyChange={setSelectedImprovementStrategy}
        onImprove={handleImprove}
        onRunBenchmark={handleRunBenchmark}
      />
      <main className="main-area">
        <RouteCanvas points={points} route={currentRoute} />
        <BenchmarkTable results={benchmarkResults} />
      </main>
    </div>
  );
}
