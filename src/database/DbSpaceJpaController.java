/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author pepe
 */
public class DbSpaceJpaController implements Serializable {

    public DbSpaceJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DbSpace dbSpace) {
        if (dbSpace.getDbBallCollection() == null) {
            dbSpace.setDbBallCollection(new ArrayList<DbBall>());
        }
        if (dbSpace.getDbObstacleCollection() == null) {
            dbSpace.setDbObstacleCollection(new ArrayList<DbObstacle>());
        }
        if (dbSpace.getDbStopitemCollection() == null) {
            dbSpace.setDbStopitemCollection(new ArrayList<DbStopitem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<DbBall> attachedDbBallCollection = new ArrayList<DbBall>();
            for (DbBall dbBallCollectionDbBallToAttach : dbSpace.getDbBallCollection()) {
                dbBallCollectionDbBallToAttach = em.getReference(dbBallCollectionDbBallToAttach.getClass(), dbBallCollectionDbBallToAttach.getId());
                attachedDbBallCollection.add(dbBallCollectionDbBallToAttach);
            }
            dbSpace.setDbBallCollection(attachedDbBallCollection);
            Collection<DbObstacle> attachedDbObstacleCollection = new ArrayList<DbObstacle>();
            for (DbObstacle dbObstacleCollectionDbObstacleToAttach : dbSpace.getDbObstacleCollection()) {
                dbObstacleCollectionDbObstacleToAttach = em.getReference(dbObstacleCollectionDbObstacleToAttach.getClass(), dbObstacleCollectionDbObstacleToAttach.getId());
                attachedDbObstacleCollection.add(dbObstacleCollectionDbObstacleToAttach);
            }
            dbSpace.setDbObstacleCollection(attachedDbObstacleCollection);
            Collection<DbStopitem> attachedDbStopitemCollection = new ArrayList<DbStopitem>();
            for (DbStopitem dbStopitemCollectionDbStopitemToAttach : dbSpace.getDbStopitemCollection()) {
                dbStopitemCollectionDbStopitemToAttach = em.getReference(dbStopitemCollectionDbStopitemToAttach.getClass(), dbStopitemCollectionDbStopitemToAttach.getId());
                attachedDbStopitemCollection.add(dbStopitemCollectionDbStopitemToAttach);
            }
            dbSpace.setDbStopitemCollection(attachedDbStopitemCollection);
            em.persist(dbSpace);
            for (DbBall dbBallCollectionDbBall : dbSpace.getDbBallCollection()) {
                DbSpace oldIdSpaceOfDbBallCollectionDbBall = dbBallCollectionDbBall.getIdSpace();
                dbBallCollectionDbBall.setIdSpace(dbSpace);
                dbBallCollectionDbBall = em.merge(dbBallCollectionDbBall);
                if (oldIdSpaceOfDbBallCollectionDbBall != null) {
                    oldIdSpaceOfDbBallCollectionDbBall.getDbBallCollection().remove(dbBallCollectionDbBall);
                    oldIdSpaceOfDbBallCollectionDbBall = em.merge(oldIdSpaceOfDbBallCollectionDbBall);
                }
            }
            for (DbObstacle dbObstacleCollectionDbObstacle : dbSpace.getDbObstacleCollection()) {
                DbSpace oldIdSpaceOfDbObstacleCollectionDbObstacle = dbObstacleCollectionDbObstacle.getIdSpace();
                dbObstacleCollectionDbObstacle.setIdSpace(dbSpace);
                dbObstacleCollectionDbObstacle = em.merge(dbObstacleCollectionDbObstacle);
                if (oldIdSpaceOfDbObstacleCollectionDbObstacle != null) {
                    oldIdSpaceOfDbObstacleCollectionDbObstacle.getDbObstacleCollection().remove(dbObstacleCollectionDbObstacle);
                    oldIdSpaceOfDbObstacleCollectionDbObstacle = em.merge(oldIdSpaceOfDbObstacleCollectionDbObstacle);
                }
            }
            for (DbStopitem dbStopitemCollectionDbStopitem : dbSpace.getDbStopitemCollection()) {
                DbSpace oldIdSpaceOfDbStopitemCollectionDbStopitem = dbStopitemCollectionDbStopitem.getIdSpace();
                dbStopitemCollectionDbStopitem.setIdSpace(dbSpace);
                dbStopitemCollectionDbStopitem = em.merge(dbStopitemCollectionDbStopitem);
                if (oldIdSpaceOfDbStopitemCollectionDbStopitem != null) {
                    oldIdSpaceOfDbStopitemCollectionDbStopitem.getDbStopitemCollection().remove(dbStopitemCollectionDbStopitem);
                    oldIdSpaceOfDbStopitemCollectionDbStopitem = em.merge(oldIdSpaceOfDbStopitemCollectionDbStopitem);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DbSpace dbSpace) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DbSpace persistentDbSpace = em.find(DbSpace.class, dbSpace.getId());
            Collection<DbBall> dbBallCollectionOld = persistentDbSpace.getDbBallCollection();
            Collection<DbBall> dbBallCollectionNew = dbSpace.getDbBallCollection();
            Collection<DbObstacle> dbObstacleCollectionOld = persistentDbSpace.getDbObstacleCollection();
            Collection<DbObstacle> dbObstacleCollectionNew = dbSpace.getDbObstacleCollection();
            Collection<DbStopitem> dbStopitemCollectionOld = persistentDbSpace.getDbStopitemCollection();
            Collection<DbStopitem> dbStopitemCollectionNew = dbSpace.getDbStopitemCollection();
            List<String> illegalOrphanMessages = null;
            for (DbBall dbBallCollectionOldDbBall : dbBallCollectionOld) {
                if (!dbBallCollectionNew.contains(dbBallCollectionOldDbBall)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DbBall " + dbBallCollectionOldDbBall + " since its idSpace field is not nullable.");
                }
            }
            for (DbObstacle dbObstacleCollectionOldDbObstacle : dbObstacleCollectionOld) {
                if (!dbObstacleCollectionNew.contains(dbObstacleCollectionOldDbObstacle)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DbObstacle " + dbObstacleCollectionOldDbObstacle + " since its idSpace field is not nullable.");
                }
            }
            for (DbStopitem dbStopitemCollectionOldDbStopitem : dbStopitemCollectionOld) {
                if (!dbStopitemCollectionNew.contains(dbStopitemCollectionOldDbStopitem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DbStopitem " + dbStopitemCollectionOldDbStopitem + " since its idSpace field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<DbBall> attachedDbBallCollectionNew = new ArrayList<DbBall>();
            for (DbBall dbBallCollectionNewDbBallToAttach : dbBallCollectionNew) {
                dbBallCollectionNewDbBallToAttach = em.getReference(dbBallCollectionNewDbBallToAttach.getClass(), dbBallCollectionNewDbBallToAttach.getId());
                attachedDbBallCollectionNew.add(dbBallCollectionNewDbBallToAttach);
            }
            dbBallCollectionNew = attachedDbBallCollectionNew;
            dbSpace.setDbBallCollection(dbBallCollectionNew);
            Collection<DbObstacle> attachedDbObstacleCollectionNew = new ArrayList<DbObstacle>();
            for (DbObstacle dbObstacleCollectionNewDbObstacleToAttach : dbObstacleCollectionNew) {
                dbObstacleCollectionNewDbObstacleToAttach = em.getReference(dbObstacleCollectionNewDbObstacleToAttach.getClass(), dbObstacleCollectionNewDbObstacleToAttach.getId());
                attachedDbObstacleCollectionNew.add(dbObstacleCollectionNewDbObstacleToAttach);
            }
            dbObstacleCollectionNew = attachedDbObstacleCollectionNew;
            dbSpace.setDbObstacleCollection(dbObstacleCollectionNew);
            Collection<DbStopitem> attachedDbStopitemCollectionNew = new ArrayList<DbStopitem>();
            for (DbStopitem dbStopitemCollectionNewDbStopitemToAttach : dbStopitemCollectionNew) {
                dbStopitemCollectionNewDbStopitemToAttach = em.getReference(dbStopitemCollectionNewDbStopitemToAttach.getClass(), dbStopitemCollectionNewDbStopitemToAttach.getId());
                attachedDbStopitemCollectionNew.add(dbStopitemCollectionNewDbStopitemToAttach);
            }
            dbStopitemCollectionNew = attachedDbStopitemCollectionNew;
            dbSpace.setDbStopitemCollection(dbStopitemCollectionNew);
            dbSpace = em.merge(dbSpace);
            for (DbBall dbBallCollectionNewDbBall : dbBallCollectionNew) {
                if (!dbBallCollectionOld.contains(dbBallCollectionNewDbBall)) {
                    DbSpace oldIdSpaceOfDbBallCollectionNewDbBall = dbBallCollectionNewDbBall.getIdSpace();
                    dbBallCollectionNewDbBall.setIdSpace(dbSpace);
                    dbBallCollectionNewDbBall = em.merge(dbBallCollectionNewDbBall);
                    if (oldIdSpaceOfDbBallCollectionNewDbBall != null && !oldIdSpaceOfDbBallCollectionNewDbBall.equals(dbSpace)) {
                        oldIdSpaceOfDbBallCollectionNewDbBall.getDbBallCollection().remove(dbBallCollectionNewDbBall);
                        oldIdSpaceOfDbBallCollectionNewDbBall = em.merge(oldIdSpaceOfDbBallCollectionNewDbBall);
                    }
                }
            }
            for (DbObstacle dbObstacleCollectionNewDbObstacle : dbObstacleCollectionNew) {
                if (!dbObstacleCollectionOld.contains(dbObstacleCollectionNewDbObstacle)) {
                    DbSpace oldIdSpaceOfDbObstacleCollectionNewDbObstacle = dbObstacleCollectionNewDbObstacle.getIdSpace();
                    dbObstacleCollectionNewDbObstacle.setIdSpace(dbSpace);
                    dbObstacleCollectionNewDbObstacle = em.merge(dbObstacleCollectionNewDbObstacle);
                    if (oldIdSpaceOfDbObstacleCollectionNewDbObstacle != null && !oldIdSpaceOfDbObstacleCollectionNewDbObstacle.equals(dbSpace)) {
                        oldIdSpaceOfDbObstacleCollectionNewDbObstacle.getDbObstacleCollection().remove(dbObstacleCollectionNewDbObstacle);
                        oldIdSpaceOfDbObstacleCollectionNewDbObstacle = em.merge(oldIdSpaceOfDbObstacleCollectionNewDbObstacle);
                    }
                }
            }
            for (DbStopitem dbStopitemCollectionNewDbStopitem : dbStopitemCollectionNew) {
                if (!dbStopitemCollectionOld.contains(dbStopitemCollectionNewDbStopitem)) {
                    DbSpace oldIdSpaceOfDbStopitemCollectionNewDbStopitem = dbStopitemCollectionNewDbStopitem.getIdSpace();
                    dbStopitemCollectionNewDbStopitem.setIdSpace(dbSpace);
                    dbStopitemCollectionNewDbStopitem = em.merge(dbStopitemCollectionNewDbStopitem);
                    if (oldIdSpaceOfDbStopitemCollectionNewDbStopitem != null && !oldIdSpaceOfDbStopitemCollectionNewDbStopitem.equals(dbSpace)) {
                        oldIdSpaceOfDbStopitemCollectionNewDbStopitem.getDbStopitemCollection().remove(dbStopitemCollectionNewDbStopitem);
                        oldIdSpaceOfDbStopitemCollectionNewDbStopitem = em.merge(oldIdSpaceOfDbStopitemCollectionNewDbStopitem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dbSpace.getId();
                if (findDbSpace(id) == null) {
                    throw new NonexistentEntityException("The dbSpace with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DbSpace dbSpace;
            try {
                dbSpace = em.getReference(DbSpace.class, id);
                dbSpace.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dbSpace with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DbBall> dbBallCollectionOrphanCheck = dbSpace.getDbBallCollection();
            for (DbBall dbBallCollectionOrphanCheckDbBall : dbBallCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DbSpace (" + dbSpace + ") cannot be destroyed since the DbBall " + dbBallCollectionOrphanCheckDbBall + " in its dbBallCollection field has a non-nullable idSpace field.");
            }
            Collection<DbObstacle> dbObstacleCollectionOrphanCheck = dbSpace.getDbObstacleCollection();
            for (DbObstacle dbObstacleCollectionOrphanCheckDbObstacle : dbObstacleCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DbSpace (" + dbSpace + ") cannot be destroyed since the DbObstacle " + dbObstacleCollectionOrphanCheckDbObstacle + " in its dbObstacleCollection field has a non-nullable idSpace field.");
            }
            Collection<DbStopitem> dbStopitemCollectionOrphanCheck = dbSpace.getDbStopitemCollection();
            for (DbStopitem dbStopitemCollectionOrphanCheckDbStopitem : dbStopitemCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DbSpace (" + dbSpace + ") cannot be destroyed since the DbStopitem " + dbStopitemCollectionOrphanCheckDbStopitem + " in its dbStopitemCollection field has a non-nullable idSpace field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(dbSpace);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DbSpace> findDbSpaceEntities() {
        return findDbSpaceEntities(true, -1, -1);
    }

    public List<DbSpace> findDbSpaceEntities(int maxResults, int firstResult) {
        return findDbSpaceEntities(false, maxResults, firstResult);
    }

    private List<DbSpace> findDbSpaceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DbSpace.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public DbSpace findDbSpace(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DbSpace.class, id);
        } finally {
            em.close();
        }
    }

    public int getDbSpaceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DbSpace> rt = cq.from(DbSpace.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
