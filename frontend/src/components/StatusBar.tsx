interface StatusBarProps {
  loading: boolean;
  error: string | null;
}

export function StatusBar({ loading, error }: StatusBarProps) {
  if (!loading && !error) return null;

  return (
    <div
      style={{
        padding: '8px 0',
        fontSize: 13,
        color: error ? '#c62828' : '#1976d2',
      }}
    >
      {loading ? '⟳ Loading…' : error}
    </div>
  );
}
